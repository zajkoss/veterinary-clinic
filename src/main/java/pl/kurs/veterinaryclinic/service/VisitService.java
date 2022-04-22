package pl.kurs.veterinaryclinic.service;

import org.springframework.stereotype.Service;
import pl.kurs.veterinaryclinic.exception.*;
import pl.kurs.veterinaryclinic.model.ConfirmationToken;
import pl.kurs.veterinaryclinic.model.Visit;
import pl.kurs.veterinaryclinic.repository.ConfirmationTokenRepository;
import pl.kurs.veterinaryclinic.repository.VisitRepository;

import javax.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class VisitService implements IVisitService {

    private VisitRepository repository;

    private ConfirmationTokenRepository confirmationTokenRepository;

    public VisitService(VisitRepository repository, ConfirmationTokenRepository confirmationTokenRepository) {
        this.repository = repository;
        this.confirmationTokenRepository = confirmationTokenRepository;
    }

    @Override
    public Visit add(Visit visit) {
        if (visit == null)
            throw new NoEntityException();
        if (visit.getId() != null)
            throw new NoEmptyIdException(visit.getId());
        //future
        if(visit.getTime().isBefore(LocalDateTime.now()))
            throw new VisitTimeException("Visit can only be scheduled in future", visit.getTime());

        // full hour
        if (visit.getTime().getMinute() != 0 || visit.getTime().getSecond() != 0)
            throw new VisitTimeException("Visit can only be scheduled on full hour", visit.getTime());

        // 8 - 20
        if (visit.getTime().getHour() < 8 || visit.getTime().getHour() > 20)
            throw new VisitTimeException("Visit can only be scheduled between 8 a.m and 8 p.m", visit.getTime());

        if (repository.findByDoctorIdAndTime(visit.getDoctor().getId(), visit.getTime()).isPresent())
            throw new VisitMemberException("Doctor already has visit that date");

        if (repository.findByPatientIdAndTime(visit.getPatient().getId(), visit.getTime()).isPresent())
            throw new VisitMemberException("Patient already has visit that date");

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
    public void delete(String  token) {
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
    public void createConfirmationToken(Visit visit, String token) {
        ConfirmationToken confirmationToken = new ConfirmationToken(token, visit);
        confirmationTokenRepository.save(confirmationToken);
    }

    @Override
    public Optional<ConfirmationToken> getConfirmationToken(String confirmationToken) {
        return confirmationTokenRepository.findByToken(confirmationToken);
    }


}
