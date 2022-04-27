package pl.kurs.veterinaryclinic.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.kurs.veterinaryclinic.model.Doctor;

import java.util.Optional;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    Optional<Doctor> findDoctorByNipEquals(String nipNumber);
    Optional<Doctor> findDoctorByIdAndIsActiveTrue(Long id);
    boolean existsByNip(String nipNumber);
}
