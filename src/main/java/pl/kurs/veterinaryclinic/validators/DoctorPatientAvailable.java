package pl.kurs.veterinaryclinic.validators;


import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = {DoctorPatientAvailableValidator.class})
@Target(value = {ElementType.TYPE})
@Retention(value = RetentionPolicy.RUNTIME)
public @interface DoctorPatientAvailable {

    String message() default "{doctorpatientavailable.message}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
