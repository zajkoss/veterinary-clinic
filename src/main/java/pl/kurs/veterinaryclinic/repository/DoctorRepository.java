package pl.kurs.veterinaryclinic.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.kurs.veterinaryclinic.model.Doctor;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
}
