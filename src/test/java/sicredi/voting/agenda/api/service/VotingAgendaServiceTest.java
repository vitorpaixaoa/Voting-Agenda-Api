package sicredi.voting.agenda.api.service;

import org.apache.commons.lang3.time.DateUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.test.context.ActiveProfiles;
import sicredi.voting.agenda.api.config.exception.BadRequestException;
import sicredi.voting.agenda.api.dto.VoteCountingDTO;
import sicredi.voting.agenda.api.dto.VoteDTO;
import sicredi.voting.agenda.api.dto.VotingAgendaDTO;
import sicredi.voting.agenda.api.mapper.VotingAgendaMapper;
import sicredi.voting.agenda.api.model.VoteModel;
import sicredi.voting.agenda.api.model.VotingAgendaModel;
import sicredi.voting.agenda.api.repository.VoteRepository;
import sicredi.voting.agenda.api.repository.VotingAgendaRepository;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureTestDatabase(replace=AutoConfigureTestDatabase.Replace.NONE)
class VotingAgendaServiceTest {



    @Mock
    private  VotingAgendaRepository repository;
    @Mock
    private  VoteRepository voteRepositoryMock;
    @Mock
    private  VotingAgendaMapper mapper;
    @Mock
    private  KafkaTemplate<String, VoteCountingDTO> kafkaTemplate;
    @Mock
    private  SimpMessagingTemplate template;



    @Autowired
    private  VoteRepository voteRepository;

    @Autowired
    private  VotingAgendaRepository repositoryLocal;

    @InjectMocks
    @Autowired
    private VotingAgendaService service;

    private VotingAgendaModel agenda;

    @BeforeEach
    public void setup(){

        agenda = VotingAgendaModel.builder()
                .id(1L)
                .dateStart(new Date())
                .dateEnd(DateUtils.addMinutes(new Date(), 1))
                .name("Agenda Test")
                .description("This is a teste.")
                .status("OPEN")
                .build();

        repositoryLocal.save(agenda);

        VoteModel voteSim = VoteModel.builder()
                .id(1L)
                .votingAgenda(agenda)
                .userCpf("12345678910")
                .voteValue("SIM")
                .build();

        VoteModel voteNao = VoteModel.builder()
                .id(2L)
                .votingAgenda(agenda)
                .userCpf("12345678911")
                .voteValue("SIM")
                .build();
        voteRepository.save(voteSim);
        voteRepository.save(voteNao);

    }


    @AfterEach
    public void tearDown(){
        voteRepository.deleteAll();
        repository.deleteAll();
    }



    @Test
    void createVotingAgendaPassingDTOShouldReturnNewAgenda(){
        VotingAgendaDTO createAgenda  = VotingAgendaDTO.builder()
                .dateStart(new Date())
                .dateEnd(DateUtils.addMinutes(new Date(), 1))
                .name("Agenda Test")
                .description("This is a teste.")
                .build();
        VotingAgendaDTO newAgenda = service.createVotingAgenda(createAgenda);

        when(repository.save(any())).thenReturn(VotingAgendaDTO.class);
        assertEquals(createAgenda.getName(), newAgenda.getName());
    }


    @Test
    void voteInAVotingAgendaByIdShouldReturnVote(){
        VoteDTO newVote = VoteDTO.builder()
                .cpf("12345678933")
                .vote("SIM")
                .build();

        VoteModel createdVote = service.addVote(1L,newVote);
        assertEquals(newVote.getCpf(), createdVote.getUserCpf());
    }

    @Test
    void voteInAVotingAgendaByWithAlreadyUsedCPFShouldReturnBadRequest(){
        VoteDTO newVote = VoteDTO.builder()
                .cpf("12345678910")
                .vote("SIM")
                .build();

        assertThrows(BadRequestException.class, () -> {
            service.addVote(1L,newVote);
        });
    }
}
























