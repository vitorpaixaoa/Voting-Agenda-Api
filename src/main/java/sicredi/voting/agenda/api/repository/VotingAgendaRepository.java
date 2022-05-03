package sicredi.voting.agenda.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import sicredi.voting.agenda.api.model.VotingAgendaModel;

import java.util.List;

public interface VotingAgendaRepository extends JpaRepository<VotingAgendaModel, Long> {

    @Query("select v from VotingAgendaModel v where v.status = 'OPEN'")
    List<VotingAgendaModel> findOpenAgendas();



}
