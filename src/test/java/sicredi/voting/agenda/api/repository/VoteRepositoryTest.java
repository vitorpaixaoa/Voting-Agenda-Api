package sicredi.voting.agenda.api.repository;

import org.apache.commons.lang3.time.DateUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import sicredi.voting.agenda.api.model.VoteModel;
import sicredi.voting.agenda.api.model.VotingAgendaModel;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace=AutoConfigureTestDatabase.Replace.NONE)
class VoteRepositoryTest {

    @Autowired
    private VoteRepository voteRepository;

    @Autowired
    private VotingAgendaRepository votingAgendaRepository;

    private VotingAgendaModel agenda;

    private VoteModel vote;

    @BeforeEach
    public void setup(){

        agenda = VotingAgendaModel.builder()
                .dateStart(new Date())
                .dateEnd(DateUtils.addMinutes(new Date(), 1))
                .name("Agenda Test")
                .description("This is a teste.")
                .status("OPEN")
                .build();


        vote = VoteModel.builder()
                .votingAgenda(agenda)
                .userCpf("12345678910")
                .voteValue("SIM")
                .build();

        votingAgendaRepository.save(agenda);
        voteRepository.save(vote);
    }

    @AfterEach
    public void tearDown(){
        voteRepository.deleteAll();
    }


    @Test
    void checkUniquenessByExistingAgendaAndCpfShouldReturnTrue(){
        assertTrue(voteRepository.existsByUserCpfAndVotingAgendaId(vote.getUserCpf(), agenda.getId()));
    }

    @Test
    void checkUniquenessByExistingAgendaAndUnsavedCpfShouldReturnFalse(){
        assertFalse(voteRepository.existsByUserCpfAndVotingAgendaId("0700000000", agenda.getId()));
    }

    @Test
    void countVotesByAgendaIdAndValueSIMShouldReturnANumber(){
        assertEquals(1, voteRepository.countByVotingAgendaIdAndVoteValue(agenda.getId(), "SIM"));
    }

    @Test
    void countVotesByAgendaIdAndValueNAOShouldReturnANumber(){
        VoteModel naoVote = vote = VoteModel.builder()
                .votingAgenda(agenda)
                .userCpf("12345678910")
                .voteValue("NAO")
                .build();
        voteRepository.save(naoVote);

        assertEquals(1, voteRepository.countByVotingAgendaIdAndVoteValue(agenda.getId(), "NAO"));
    }

    @Test
    void voteInAVotingAgendaByIdShouldReturnVoteValu(){
        VoteModel createVote  = VoteModel.builder()
                .votingAgenda(agenda)
                .userCpf("12345678910")
                .voteValue("NAO")
                .build();

        VoteModel newVote = voteRepository.save(createVote);

        assertEquals(newVote.getUserCpf(), createVote.getUserCpf());
    }

}
