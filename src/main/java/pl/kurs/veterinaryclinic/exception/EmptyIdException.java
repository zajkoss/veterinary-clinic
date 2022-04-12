package pl.kurs.veterinaryclinic.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class EmptyIdException extends RuntimeException {

    private Long id;

    public EmptyIdException() {
        super("null");
    }

    public EmptyIdException(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

}
