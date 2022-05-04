package sicredi.voting.agenda.api.repository;

import org.apache.commons.lang3.time.DateUtils;
import org.junit.jupiter.api.AfterEach;
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
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace=AutoConfigureTestDatabase.Replace.NONE)
class VotingAgendaRepositoryTest {

    @Autowired
    private VotingAgendaRepository votingAgendaRepository;

    @BeforeEach
    public void setup(){

        VotingAgendaModel agenda = VotingAgendaModel.builder()
                .id(1L)
                .dateStart(new Date())
                .dateEnd(DateUtils.addMinutes(new Date(), 1))
                .name("Agenda Test")
                .description("This is a teste.")
                .status("OPEN")
                .build();

        votingAgendaRepository.save(agenda);
    }

    @AfterEach
    public void tearDown(){
        votingAgendaRepository.deleteAll();
    }

    @Test
    void voteInAVotingAgendaByIdShouldReturnVoteValu(){
        VotingAgendaModel createAgenda  = VotingAgendaModel.builder()
                .dateStart(new Date())
                .dateEnd(DateUtils.addMinutes(new Date(), 1))
                .name("Agenda Test")
                .description("This is a teste.")
                .status("OPEN")
                .build();

        votingAgendaRepository.save(createAgenda);

        VotingAgendaModel newAgenda = votingAgendaRepository.save(createAgenda);

        assertEquals(createAgenda.getName(), newAgenda.getName());
    }

    @Test
    void findAllOpenedAgendasShouldntReturnWithStatusClose(){
        VotingAgendaModel createAgenda  = VotingAgendaModel.builder()
                .dateStart(new Date())
                .dateEnd(DateUtils.addMinutes(new Date(), 1))
                .name("ClosedAgenda")
                .description("This is a teste.")
                .status("CLOSE")
                .build();

        votingAgendaRepository.save(createAgenda);

        List<VotingAgendaModel> openList = votingAgendaRepository.findOpenAgendas();
        assertFalse(openList.stream().anyMatch(item -> item.getStatus().equals("CLOSE") ));
    }

}
