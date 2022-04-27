package pl.kurs.veterinaryclinic.validators;

import pl.kurs.veterinaryclinic.repository.DoctorRepository;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;

public class UniqueNIPNumberValidator implements ConstraintValidator<UniqueNIPNumber, String> {


    private DoctorRepository doctorRepository;

    public UniqueNIPNumberValidator(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    @Override
    public void initialize(UniqueNIPNumber constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String nip, ConstraintValidatorContext constraintValidatorContext) {
        return !doctorRepository.existsByNip(nip);
    }
}
