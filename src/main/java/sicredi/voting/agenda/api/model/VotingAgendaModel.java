package sicredi.voting.agenda.api.model;

import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.List;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "VOTING_AGENDA")
public class VotingAgendaModel {

    @Id
    @Column(name = "VOTING_AGENDA_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "NAME", nullable = false)
    String name;

    @Column(name = "DESCRIPTION", nullable = false)
    String description;

    @Column(name = "START", nullable = false)
    Date dateStart = new Date();

    @Column(name = "STATUS", nullable = false)
    String status = "OPEN";

    @Column(name = "END")
    Date dateEnd;


    @OneToMany(mappedBy = "votingAgenda")
    List<VoteModel> votes;


    public boolean isOpen() {
        return dateEnd.after(new Date());
    }
}
