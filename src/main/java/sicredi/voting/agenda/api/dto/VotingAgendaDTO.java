package sicredi.voting.agenda.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VotingAgendaDTO implements Serializable {

    Long id;

    @NotNull
    String name;

    @NotNull
    String description;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "America/Sao_Paulo")
    Date dateStart;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm",timezone = "America/Sao_Paulo")
    Date dateEnd;

    boolean isOpen;

}
