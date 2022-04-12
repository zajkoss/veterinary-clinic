package pl.kurs.veterinaryclinic.service;

import pl.kurs.veterinaryclinic.model.Doctor;

import java.util.List;

public interface IDoctorService {

    Doctor add(Doctor doctor);
    Doctor get(Long id);
    List<Doctor> getAll();
    void softDelete(Long id);

}
