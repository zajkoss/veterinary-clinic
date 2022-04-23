package pl.kurs.veterinaryclinic.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;
import java.util.List;

@Documented
@Constraint(validatedBy = {EnumsValidatorValidator.class})
@Target(value = {ElementType.PARAMETER, ElementType.FIELD})
@Retention(value = RetentionPolicy.RUNTIME)
public @interface EnumsValidator {

    Class<? extends Enum<?>> enumClass();
    String message() default "Invalid value for: {enumClass}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
