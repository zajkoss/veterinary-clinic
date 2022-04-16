package pl.kurs.veterinaryclinic.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EnumsValidatorValidator implements ConstraintValidator<EnumsValidator, CharSequence> {
    private List<String> availableValues ;

    @Override
    public void initialize(EnumsValidator constraintAnnotation) {
        availableValues = Stream.of(constraintAnnotation.enumClass().getEnumConstants())
                .map(Enum::name)
                .collect(Collectors.toList());
    }

    @Override
    public boolean isValid(CharSequence charSequence, ConstraintValidatorContext constraintValidatorContext) {
        if (charSequence == null) {
            return true;
        }
        return availableValues.contains(charSequence.toString());
    }
}
