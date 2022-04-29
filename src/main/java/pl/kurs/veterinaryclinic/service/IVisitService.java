package pl.kurs.veterinaryclinic.service;

import pl.kurs.veterinaryclinic.model.ConfirmationToken;
import pl.kurs.veterinaryclinic.model.Visit;
import pl.kurs.veterinaryclinic.model.enums.AnimalType;
import pl.kurs.veterinaryclinic.model.enums.DoctorType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface IVisitService {

    Visit add(Visit visit);

    void confirm(String token);

    void delete(String token);

    List<Visit> findAllVisitInTime(
            LocalDateTime fromTime,
            LocalDateTime toTime
    );

    List<Visit> findAllAvailableVisitInTimeByDoctorTypeAndAnimal(
            LocalDateTime fromTime,
            LocalDateTime toTime,
            DoctorType doctorType,
            AnimalType animalType
    );

    List<Visit> findAllVisitForNextDayWithoutSendReminder();

    void setReminderOfVisit(Visit visit);

    void createConfirmationToken(Visit visit, String token);

    Optional<ConfirmationToken> getConfirmationToken(String confirmationToken);

}
