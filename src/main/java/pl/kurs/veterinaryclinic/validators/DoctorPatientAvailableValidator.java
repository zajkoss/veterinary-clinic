package pl.kurs.veterinaryclinic.validators;

import org.springframework.transaction.annotation.Transactional;
import pl.kurs.veterinaryclinic.commands.CreateDoctorCommand;
import pl.kurs.veterinaryclinic.commands.CreateVisitCommand;
import pl.kurs.veterinaryclinic.repository.VisitRepository;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class DoctorPatientAvailableValidator implements ConstraintValidator<DoctorPatientAvailable, CreateVisitCommand> {

    private VisitRepository repository;

    public DoctorPatientAvailableValidator(VisitRepository repository) {
        this.repository = repository;
    }

    @Override
    public void initialize(DoctorPatientAvailable constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    @Transactional
    public boolean isValid(CreateVisitCommand createVisitCommand, ConstraintValidatorContext constraintValidatorContext) {
        if (repository.findByDoctorIdAndTime(createVisitCommand.getDoctorIdentity(), createVisitCommand.getTime()).isPresent())
            return false;

        if (repository.findByPatientIdAndTime(createVisitCommand.getPatientIdentity(), createVisitCommand.getTime()).isPresent())
            return false;
        return true;
    }
}
