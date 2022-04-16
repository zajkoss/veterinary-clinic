package pl.kurs.veterinaryclinic.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NotFoundRelationException extends RuntimeException {

    private Long id;

    public NotFoundRelationException(String message, Long id) {
        super(message + ", id=" + id);
        this.id = id;
    }

    public Long getToken() {
        return id;
    }


}
