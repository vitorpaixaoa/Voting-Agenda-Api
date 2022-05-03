package sicredi.voting.agenda.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.Check;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "VOTE")
public class VoteModel {

    @Id
    @Column(name = "VOTING_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;


    @Column(name = "USER_CPF", nullable = false)
    String userCpf;

    @Check(constraints = "IN_VALUE IN ('S' , 'N')")
    @Column(name = "VOTE_VALUE", nullable = false)
    String voteValue;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "VOTING_AGENDA_ID", referencedColumnName = "VOTING_AGENDA_ID", nullable = false)
    VotingAgendaModel votingAgenda;


}
