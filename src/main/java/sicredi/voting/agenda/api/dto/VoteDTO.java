package sicredi.voting.agenda.api.dto;

import lombok.Data;
import org.hibernate.validator.constraints.br.CPF;

import javax.validation.constraints.NotNull;

@Data
public class VoteDTO {

    @NotNull
    @CPF(message = "Formato de cpf inválido")
    String cpf;

    @NotNull
    String vote;

}
