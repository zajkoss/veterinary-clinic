package pl.kurs.veterinaryclinic.service;

import org.springframework.stereotype.Service;
import pl.kurs.veterinaryclinic.dto.AvailableVisitDto;
import pl.kurs.veterinaryclinic.dto.DoctorNameDto;
import pl.kurs.veterinaryclinic.exception.*;
import pl.kurs.veterinaryclinic.model.ConfirmationToken;
import pl.kurs.veterinaryclinic.model.Doctor;
import pl.kurs.veterinaryclinic.model.Visit;
import pl.kurs.veterinaryclinic.model.enums.AnimalType;
import pl.kurs.veterinaryclinic.model.enums.DoctorType;
import pl.kurs.veterinaryclinic.repository.ConfirmationTokenRepository;
import pl.kurs.veterinaryclinic.repository.VisitRepository;

import javax.persistence.EntityNotFoundException;

import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class VisitService implements IVisitService {

    private VisitRepository repository;

    private IDoctorService doctorService;

    private ConfirmationTokenRepository confirmationTokenRepository;

    public VisitService(VisitRepository repository, IDoctorService doctorService, ConfirmationTokenRepository confirmationTokenRepository) {
        this.repository = repository;
        this.doctorService = doctorService;
        this.confirmationTokenRepository = confirmationTokenRepository;
    }

    @Override
    public Visit add(Visit visit) {
        if (visit == null)
            throw new NoEntityException();
        if (visit.getId() != null)
            throw new NoEmptyIdException(visit.getId());

        return repository.save(visit);
    }

    @Override
    public void confirm(String token) {
        ConfirmationToken confirmationToken = this.getConfirmationToken(token).orElseThrow(() -> new EntityNotFoundException("Wrong token or visit was canceled"));
        Visit visit = confirmationToken.getVisit();
        if (visit.getConfirmed())
            throw new WrongVisitStatusException("Visit already confirmed", visit.getId());

        LocalDateTime executeTime = LocalDateTime.now();
        if (executeTime.isAfter(confirmationToken.getExpiryDate()))
            throw new VisitTimeException("Too late to confirm visit", visit.getId(), executeTime, confirmationToken.getExpiryDate());

        visit.setConfirmed(true);
        repository.save(visit);
    }

    @Override
    public void delete(String token) {
        ConfirmationToken confirmationToken = this.getConfirmationToken(token).orElseThrow(() -> new EntityNotFoundException("Wrong token or visit was canceled"));
        Visit visit = confirmationToken.getVisit();

        repository.findById(visit.getId())
                .orElseThrow(() -> new EntityNotFoundException("Wrong token or visit was canceled"));

        LocalDateTime executeTime = LocalDateTime.now();
        if (executeTime.isAfter(visit.getTime().minusHours(1)))
            throw new VisitTimeException("Too late to cancel visit", visit.getId(), executeTime, visit.getTime());

        confirmationTokenRepository.deleteAllByVisitId(visit.getId());
        repository.delete(visit);
    }

    @Override
    public List<Visit> findAllVisitInTime(LocalDateTime fromTime, LocalDateTime toTime) {
        return repository.findAllByTimeAfterAndTimeBeforeOrderByTime(fromTime.minusSeconds(1), toTime);
    }

    @Override
    public List<Visit> findAllAvailableVisitInTimeByDoctorTypeAndAnimal(LocalDateTime fromTime, LocalDateTime toTime, DoctorType doctorType, AnimalType animalType) {

        List<Visit> plannedVisit = findAllVisitInTime(fromTime, toTime);
        List<Doctor> foundDoctors = doctorService.getAllForParameters(doctorType, animalType);
        List<Visit> result = new ArrayList<>();
        LocalDateTime startTime = fromTime;
        if (startTime.getMinute() != 0 || startTime.getSecond() != 0)
            startTime = startTime.withMinute(0).withSecond(0).plusHours(1);

        while (startTime.isBefore(toTime)) {
            LocalDateTime finalStartTime = startTime;
            if (finalStartTime.getHour() >= 8 && finalStartTime.getHour() <= 20) {
                result.addAll(
                        foundDoctors.stream()
                                .filter(doctor -> plannedVisit.stream().noneMatch(visit -> visit.getTime().isEqual(finalStartTime) && visit.getDoctor() == doctor))
                                .map(doctor -> createNewAvailableVisit(doctor,finalStartTime))
                                .collect(Collectors.toList())
                );
            }
            startTime = startTime.plusHours(1);
        }
        return result;
    }

    private Visit createNewAvailableVisit(Doctor doctor, LocalDateTime hour) {
        Visit visit = new Visit();
        visit.setDoctor(doctor);
        visit.setTime(hour);
        return visit;
    }

    @Override
    public List<Visit> findAllVisitForNextDayWithoutSendReminder() {
        LocalDateTime fromTime = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
        LocalDateTime toTime = LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.MIN);
        System.out.println(fromTime + " " + toTime);
        return repository.findAllByTimeAfterAndTimeBeforeAndReminderSentFalse(fromTime, toTime);
    }

    @Override
    public void setReminderOfVisit(Visit visit) {
        repository.save(visit);
    }

    @Override
    public void createConfirmationToken(Visit visit, String token) {
        ConfirmationToken confirmationToken = new ConfirmationToken(token, visit);
        confirmationTokenRepository.save(confirmationToken);
    }

    @Override
    public Optional<ConfirmationToken> getConfirmationToken(String confirmationToken) {
        return confirmationTokenRepository.findByToken(confirmationToken);
    }


}
