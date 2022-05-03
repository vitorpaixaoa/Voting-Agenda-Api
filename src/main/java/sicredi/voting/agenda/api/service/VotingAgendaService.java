package sicredi.voting.agenda.api.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import sicredi.voting.agenda.api.dto.VerifyCpfDTO;
import sicredi.voting.agenda.api.dto.VoteCountingDTO;
import sicredi.voting.agenda.api.dto.VoteDTO;
import sicredi.voting.agenda.api.dto.VotingAgendaDTO;
import sicredi.voting.agenda.api.model.VoteModel;
import sicredi.voting.agenda.api.model.VotingAgendaModel;
import sicredi.voting.agenda.api.exception.BadRequestException;
import sicredi.voting.agenda.api.exception.NotFoundException;
import sicredi.voting.agenda.api.mapper.VotingAgendaMapper;
import sicredi.voting.agenda.api.repository.VoteRepository;
import sicredi.voting.agenda.api.repository.VotingAgendaRepository;

import java.util.List;
import java.util.Locale;

import static java.util.Objects.isNull;
import static sicredi.voting.agenda.api.util.Utils.checkCPF;

@Slf4j
@Service
@EnableScheduling
@RequiredArgsConstructor
public class VotingAgendaService {

    private final VotingAgendaRepository repository;
    private final VoteRepository voteRepository;
    private final VotingAgendaMapper mapper;

    private final KafkaTemplate<String, VoteCountingDTO> kafkaTemplate;

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

        if (Boolean.FALSE.equals(votingAgenda.isOpen())) {
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
            return checkCPF(cpf);
        } catch (Exception ex) {
            log.error(ex.getLocalizedMessage());
            throw new NotFoundException("Cpf inválido");
        }
    }

    private String checkVote(String vote) {
        String str = vote.toUpperCase(Locale.ROOT).replace("Ã", "A");
        if (!(str.equals("SIM") || str.equals("NAO"))) {
            throw new BadRequestException("vote", "Os votos só podem ter valor de sim ou não");
        }
        return str;
    }

    @Scheduled(fixedRate = 50000)
    private void testing() {
        List<VotingAgendaModel> list = repository.findOpenAgendas();
        list.forEach(agenda -> {
            if (Boolean.FALSE.equals(agenda.isOpen())) {
                log.info("tentando enviar req pro kafka.");
                VoteCountingDTO dto = getVotes(agenda.getId());
                try {
                    kafkaTemplate.send("voteResult", dto);
                    agenda.setStatus("CLOSED");
                    repository.save(agenda);
                    log.info("Message sent.");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

}

