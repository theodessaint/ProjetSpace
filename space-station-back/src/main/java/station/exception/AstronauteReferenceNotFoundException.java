package station.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import jakarta.persistence.EntityNotFoundException;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class AstronauteReferenceNotFoundException extends EntityNotFoundException {
    public AstronauteReferenceNotFoundException() {
        super("AstronauteReference introuvable");
    }
}