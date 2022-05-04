package sicredi.voting.agenda.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import sicredi.voting.agenda.api.model.VotingAgendaModel;

import java.util.List;

@Repository
public interface VotingAgendaRepository extends JpaRepository<VotingAgendaModel, Long> {

    @Query("select v from VotingAgendaModel v where v.status = 'OPEN'")
    List<VotingAgendaModel> findOpenAgendas();



}
