package pl.kurs.veterinaryclinic.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.kurs.veterinaryclinic.model.Visit;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface VisitRepository extends JpaRepository<Visit, Long> {

    List<Visit> findAllByTimeAfterAndTimeBeforeOrderByTime(LocalDateTime from,LocalDateTime to);
    List<Visit> findAllByTimeAfterAndTimeBeforeAndReminderSentFalse(LocalDateTime from, LocalDateTime to);
    Optional<Visit> findByDoctorIdAndTime(Long id,LocalDateTime time);
    Optional<Visit> findByPatientIdAndTime(Long id,LocalDateTime time);

}
