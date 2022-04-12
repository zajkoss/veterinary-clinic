package pl.kurs.veterinaryclinic.validators;

import org.hibernate.validator.internal.constraintvalidators.hv.pl.NIPValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = {NipValidator.class})
@Target(value = {ElementType.FIELD})
@Retention(value = RetentionPolicy.RUNTIME)
public @interface NIP {

    String message() default "{nip.message}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
