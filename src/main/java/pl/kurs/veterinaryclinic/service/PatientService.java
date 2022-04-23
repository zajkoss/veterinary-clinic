package pl.kurs.veterinaryclinic.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.kurs.veterinaryclinic.exception.EmptyIdException;
import pl.kurs.veterinaryclinic.exception.NoEmptyIdException;
import pl.kurs.veterinaryclinic.exception.NoEntityException;
import pl.kurs.veterinaryclinic.model.Patient;
import pl.kurs.veterinaryclinic.repository.PatientRepository;

import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
@Transactional
public class PatientService implements IPatientService {

    private PatientRepository repository;

    public PatientService(PatientRepository repository) {
        this.repository = repository;
    }


    @Override
    public Patient add(Patient patient) {
        if (patient == null)
            throw new NoEntityException();
        if (patient.getId() != null)
            throw new NoEmptyIdException(patient.getId());

        return repository.save(patient);
    }

    @Override
    public Optional<Patient> get(Long id) {
        return repository
                .findById(Optional.ofNullable(id).orElseThrow(() -> new EmptyIdException(id)));
    }

    @Override
    public Page<Patient> getAll(Pageable pageable) {
        return repository.findAll(pageable);
    }
}
