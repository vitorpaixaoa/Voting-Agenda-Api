package sicredi.voting.agenda.api.config.exception;


import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class NotFoundDetails {

    private String message;
    private int status;
}
