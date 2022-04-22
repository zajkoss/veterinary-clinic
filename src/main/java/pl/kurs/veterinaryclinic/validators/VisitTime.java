package pl.kurs.veterinaryclinic.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = {VisitTimeValidator.class})
@Target(value = {ElementType.FIELD})
@Retention(value = RetentionPolicy.RUNTIME)
public @interface VisitTime {

    String message() default "{visittime.message}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
