package pl.kurs.veterinaryclinic.service;

import pl.kurs.veterinaryclinic.model.Doctor;
import pl.kurs.veterinaryclinic.model.Visit;
import pl.kurs.veterinaryclinic.model.enums.AnimalType;
import pl.kurs.veterinaryclinic.model.enums.DoctorType;

import java.time.LocalDateTime;
import java.util.List;

public interface IVisitService {

    Visit add(Visit visit);
    void confirm(Long id);
    void delete(Long id);
    List<Visit> findAllVisitInTime(
            LocalDateTime fromTime,
            LocalDateTime toTime
    );

}
