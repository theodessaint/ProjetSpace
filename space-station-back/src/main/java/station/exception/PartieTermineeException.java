package station.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class PartieTermineeException extends RuntimeException {

    public PartieTermineeException() {
        super("Cette partie est déjà terminée.");
    }
}
