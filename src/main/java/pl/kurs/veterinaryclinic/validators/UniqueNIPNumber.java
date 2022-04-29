package pl.kurs.veterinaryclinic.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = {UniqueNIPNumberValidator.class})
@Target(value = {ElementType.FIELD})
@Retention(value = RetentionPolicy.RUNTIME)
public @interface UniqueNIPNumber {

    String message() default "{nipunique.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
