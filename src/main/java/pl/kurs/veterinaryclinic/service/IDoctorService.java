package pl.kurs.veterinaryclinic.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.kurs.veterinaryclinic.model.Doctor;

import java.util.List;

public interface IDoctorService {

    Doctor add(Doctor doctor);
    Doctor get(Long id);
    Page<Doctor> getAll(Pageable pageable);
    void softDelete(Long id);

}
