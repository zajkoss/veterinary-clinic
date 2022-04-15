package pl.kurs.veterinaryclinic.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.kurs.veterinaryclinic.model.Patient;

public interface IPatientService {

    Patient add(Patient patient);
    Patient get(Long id);
    Page<Patient> getAll(Pageable pageable);

}
