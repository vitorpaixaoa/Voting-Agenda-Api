package sicredi.voting.agenda.api.config.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.BAD_REQUEST)
@AllArgsConstructor
public class BadRequestException extends RuntimeException {
    
	private static final long serialVersionUID = 1L;
	
	private final String field;
    private final String message;


}
