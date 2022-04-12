package pl.kurs.veterinaryclinic.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NipValidator implements ConstraintValidator<NIP, String> {
    @Override
    public void initialize(NIP constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return s.matches("\\d{10}");
    }
}
