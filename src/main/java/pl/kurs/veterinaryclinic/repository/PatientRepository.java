package pl.kurs.veterinaryclinic.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.kurs.veterinaryclinic.model.Patient;

public interface PatientRepository extends JpaRepository<Patient, Long> {

}
