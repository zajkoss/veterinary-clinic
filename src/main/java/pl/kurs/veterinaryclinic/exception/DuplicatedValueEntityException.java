package pl.kurs.veterinaryclinic.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class DuplicatedValueEntityException extends RuntimeException{

    private String field;
    private String value;

    public DuplicatedValueEntityException(String field, String value) {
        super("Duplicated value: " + value + ", field: " + field);
        this.value = value;
        this.field = field;
    }

    public String getField() {
        return field;
    }

    public String getValue() {
        return value;
    }
}
