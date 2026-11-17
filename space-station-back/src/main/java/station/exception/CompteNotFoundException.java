package station.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import jakarta.persistence.EntityNotFoundException;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CompteNotFoundException extends EntityNotFoundException {
    public CompteNotFoundException() {
        super("Compte introuvable");
    }
}
