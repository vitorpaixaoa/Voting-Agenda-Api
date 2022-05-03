package sicredi.voting.agenda.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VoteCountingDTO {

    String voteCountingName;

    Integer sim;

    Integer nao;

}
