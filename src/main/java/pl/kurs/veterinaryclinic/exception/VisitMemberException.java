package pl.kurs.veterinaryclinic.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class VisitMemberException extends RuntimeException {

    public VisitMemberException(String message) {
        super(message);
    }



}
