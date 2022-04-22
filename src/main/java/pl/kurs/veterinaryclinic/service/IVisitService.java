package pl.kurs.veterinaryclinic.service;

import pl.kurs.veterinaryclinic.model.ConfirmationToken;
import pl.kurs.veterinaryclinic.model.Visit;

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

    void createConfirmationToken(Visit visit, String token);
    Optional<ConfirmationToken> getConfirmationToken(String confirmationToken);

}
