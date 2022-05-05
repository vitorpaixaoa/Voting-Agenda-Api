package sicredi.voting.agenda.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.br.CPF;

import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VoteDTO {

    @NotNull
    @CPF(message = "Formato de cpf inválido")
    String cpf;

    @NotNull
    String vote;

}
