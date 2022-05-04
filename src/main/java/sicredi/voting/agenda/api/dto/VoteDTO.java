package sicredi.voting.agenda.api.dto;

import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.br.CPF;

import javax.validation.constraints.NotNull;

@Data
@Builder
public class VoteDTO {

    @NotNull
    @CPF(message = "Formato de cpf inválido")
    String cpf;

    @NotNull
    String vote;

}
