package sicredi.voting.agenda.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import sicredi.voting.agenda.api.model.VoteModel;

@Repository
public interface VoteRepository extends JpaRepository<VoteModel, Long> {
    Boolean existsByUserCpfAndVotingAgendaId(String cpf, Long id);

    @Query("select count(v) from VoteModel v where v.votingAgenda.id = ?1 and upper(v.voteValue) = upper(?2) ")
    Integer countByVotingAgendaIdAndVoteValue(Long agendaId, String value);
}
