package pl.kurs.veterinaryclinic.excpetionhandlers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import pl.kurs.veterinaryclinic.exception.EmptyIdException;
import pl.kurs.veterinaryclinic.exception.NoEmptyIdException;
import pl.kurs.veterinaryclinic.exception.NoEntityException;

import java.time.LocalDateTime;
import java.util.List;

@ControllerAdvice
public class MainExceptionHandler {

    @ExceptionHandler({NoEntityException.class, EmptyIdException.class, NoEmptyIdException.class})
    public ResponseEntity<ExceptionResponse> handleCustomExceptions(Exception e) {
        ExceptionResponse response = new ExceptionResponse(List.of(e.getMessage()), e.getClass().getSimpleName(), "BAD_REQUEST", LocalDateTime.now());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
