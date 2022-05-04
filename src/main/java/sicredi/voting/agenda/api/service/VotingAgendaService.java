package sicredi.voting.agenda.api.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import sicredi.voting.agenda.api.config.KafkaConstants;
import sicredi.voting.agenda.api.config.exception.BadRequestException;
import sicredi.voting.agenda.api.config.exception.NotFoundException;
import sicredi.voting.agenda.api.dto.VerifyCpfDTO;
import sicredi.voting.agenda.api.dto.VoteCountingDTO;
import sicredi.voting.agenda.api.dto.VoteDTO;
import sicredi.voting.agenda.api.dto.VotingAgendaDTO;
import sicredi.voting.agenda.api.mapper.VotingAgendaMapper;
import sicredi.voting.agenda.api.model.VoteModel;
import sicredi.voting.agenda.api.model.VotingAgendaModel;
import sicredi.voting.agenda.api.repository.VoteRepository;
import sicredi.voting.agenda.api.repository.VotingAgendaRepository;

import java.util.List;
import java.util.Locale;

import static java.util.Objects.isNull;

@Slf4j
@Service
@EnableScheduling
@RequiredArgsConstructor
public class VotingAgendaService {

    private final VotingAgendaRepository repository;
    private final VoteRepository voteRepository;
    private final VotingAgendaMapper mapper;

    @Value("${project.cpf.url}")
    private String checkCpfUrl;

    private final KafkaTemplate<String, VoteCountingDTO> kafkaTemplate;
    private final SimpMessagingTemplate template;

    public Page<VotingAgendaDTO> allVotingAgenda(Pageable pageable) {
        return mapper.toDTO(repository.findAll(pageable));
    }

    public VotingAgendaModel findVotingAgendaById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Pauta para votação não encontrada"));
    }


    public VotingAgendaDTO createVotingAgenda(VotingAgendaDTO dto) {

        VotingAgendaModel model = mapper.toEntity(dto);
        if (isNull(model.getDateEnd())) {
            model.setDateEnd(DateUtils.addMinutes(model.getDateStart(), 1));
        }
        return mapper.toDTO(repository.save(model));
    }

    public VoteModel addVote(Long idAgenda, VoteDTO dto) {
        VotingAgendaModel votingAgenda = findVotingAgendaById(idAgenda);

        if (!votingAgenda.isOpen()) {
            throw new BadRequestException("pauta", "Esta pauta está fechada para votações.");
        }
        if (Boolean.TRUE.equals(voteRepository.existsByUserCpfAndVotingAgendaId(dto.getCpf(), idAgenda))) {
            throw new BadRequestException("cpf", "Essa pauta já possui um voto desse usuário");
        }

        VoteModel vote = VoteModel.builder()
                .votingAgenda(votingAgenda)
                .voteValue(checkVote(dto.getVote()))
                .userCpf(dto.getCpf())
                .build();

        return voteRepository.save(vote);
    }

    public VoteCountingDTO getVotes(Long id) {
        VotingAgendaModel agenda = findVotingAgendaById(id);
        Integer sim = voteRepository.countByVotingAgendaIdAndVoteValue(agenda.getId(), "SIM");
        Integer nao = voteRepository.countByVotingAgendaIdAndVoteValue(agenda.getId(), "NAO");
        return new VoteCountingDTO(agenda.getName(), sim, nao);

    }

    public VerifyCpfDTO verifyCpf(String cpf) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            return restTemplate.getForObject(checkCpfUrl + cpf, VerifyCpfDTO.class);
        } catch (Exception ex) {
            log.error(ex.getLocalizedMessage());
            throw new NotFoundException("Cpf inválido");
        }
    }

    @Scheduled(fixedRate = 10000)
    private void checkOpenedAgendas() {
        List<VotingAgendaModel> list = repository.findOpenAgendas();
        list.forEach(agenda -> {
            if (!agenda.isOpen()) {
                VoteCountingDTO dto = getVotes(agenda.getId());
                try {
                    kafkaTemplate.send(KafkaConstants.RESULT_TOPIC, dto.getVoteCountingName(), dto);
                    agenda.setStatus("CLOSED");
                    repository.save(agenda);
                    log.info("Message sent.");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @KafkaListener(
            topics = KafkaConstants.RESULT_TOPIC,
            groupId = KafkaConstants.GROUP_ID
    )
    public void listen(VoteCountingDTO dto) {
        log.info("Sending via kafka listener...");
        template.convertAndSend("/topic/group", dto);
    }

    private String checkVote(String vote) {
        String str = vote.toUpperCase(Locale.ROOT).replace("Ã", "A");
        if (!(str.equals("SIM") || str.equals("NAO"))) {
            throw new BadRequestException("vote", "Os votos só podem ter valor de sim ou não");
        }
        return str;
    }


}

