package pl.kurs.veterinaryclinic.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class WrongVisitStatusException extends RuntimeException {

    private Long token;

    public WrongVisitStatusException(String message, Long token) {
        super(message + ", token=" + token);
    }

    public Long getToken() {
        return token;
    }


}
