package pl.kurs.veterinaryclinic.validators;

import pl.kurs.veterinaryclinic.exception.VisitTimeException;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;

public class VisitTimeValidator implements ConstraintValidator<VisitTime, LocalDateTime> {

    @Override
    public void initialize(VisitTime constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(LocalDateTime time, ConstraintValidatorContext constraintValidatorContext) {
        //future
        if(time.isBefore(LocalDateTime.now()))
            return false;
        // full hour
        if (time.getMinute() != 0 || time.getSecond() != 0)
            return false;

        if (time.getHour() < 8 || time.getHour() > 20)
            return false;

        return true;
    }
}