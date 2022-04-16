package pl.kurs.veterinaryclinic.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.kurs.veterinaryclinic.model.Patient;

import java.util.Optional;

public interface IPatientService {

    Patient add(Patient patient);
    Optional<Patient> get(Long id);
    Page<Patient> getAll(Pageable pageable);

}
