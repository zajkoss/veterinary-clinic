package pl.kurs.veterinaryclinic.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.kurs.veterinaryclinic.model.Doctor;
import pl.kurs.veterinaryclinic.model.enums.AnimalType;
import pl.kurs.veterinaryclinic.model.enums.DoctorType;

import java.util.List;
import java.util.Optional;

public interface IDoctorService {

    Doctor add(Doctor doctor);

    Optional<Doctor> get(Long id);

    Optional<Doctor> getActiveById(Long id);

    Page<Doctor> getAll(Pageable pageable);

    void softDelete(Long id);

    List<Doctor> getAllForParameters(DoctorType doctorType, AnimalType animalType);

}
