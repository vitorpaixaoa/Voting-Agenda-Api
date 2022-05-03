package sicredi.voting.agenda.api.model;

import lombok.*;
import sicredi.voting.agenda.api.dto.VoteCountingDTO;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicReference;

import static java.util.Objects.isNull;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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

    public VoteCountingDTO getResultVotes() {
        AtomicReference<Integer> s = new AtomicReference<>(0);
        AtomicReference<Integer> n = new AtomicReference<>(0);
        votes.forEach(vote -> {
            if (!isNull(vote.getVoteValue())) {
                if (vote.getVoteValue().toUpperCase(Locale.ROOT).equals("SIM")) {
                    s.getAndSet(s.get() + 1);
                } else if (vote.getVoteValue().toUpperCase(Locale.ROOT).equals("NAO")) {
                    n.getAndSet(n.get() + 1);
                }
            }

        });
        return new VoteCountingDTO(
                this.name,
                s.get(),
                n.get()
        );
    }

    public Boolean isOpen() {
        return !this.dateEnd.after(new Date()) && this.dateStart.before(new Date()) ;
    }
}
