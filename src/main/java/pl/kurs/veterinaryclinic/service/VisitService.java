package pl.kurs.veterinaryclinic.service;

import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import pl.kurs.veterinaryclinic.exception.*;
import pl.kurs.veterinaryclinic.model.Doctor;
import pl.kurs.veterinaryclinic.model.Visit;
import pl.kurs.veterinaryclinic.model.enums.AnimalType;
import pl.kurs.veterinaryclinic.model.enums.DoctorType;
import pl.kurs.veterinaryclinic.repository.DoctorRepository;
import pl.kurs.veterinaryclinic.repository.VisitRepository;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class VisitService implements IVisitService {

    private VisitRepository repository;

    public VisitService(VisitRepository repository) {
        this.repository = repository;
    }

    @Override
    public Visit add(Visit visit) {
        if (visit == null)
            throw new NoEntityException();
        if (visit.getId() != null)
            throw new NoEmptyIdException(visit.getId());

        // full hour
        if (visit.getTime().getMinute() != 0 || visit.getTime().getSecond() != 0)
            throw new VisitTimeException("Visit can only be scheduled on full hour",visit.getTime());

        // 8 - 20
        if(visit.getTime().getHour() < 8 && visit.getTime().getHour() > 20)
            throw new VisitTimeException("Visit can only be scheduled between 8 a.m and 8 p.m",visit.getTime());

        return repository.save(visit);
    }

    @Override
    public void confirm(Long id) {
        Visit loadedVisit = repository
                .findById(Optional.ofNullable(id).orElseThrow(() -> new EntityNotFoundException("Wrong token")))
                .orElseThrow(() -> new EntityNotFoundException("Wrong token | Visit canceled"));

        if(loadedVisit.getConfirmed())
            throw new WrongVisitStatusException("Visit already confirmed",id);

        LocalDateTime executeTime = LocalDateTime.now();
        if(executeTime.isAfter(loadedVisit.getTime()))
            throw new VisitTimeException("Too late to confirm visit",id,executeTime,loadedVisit.getTime());

        loadedVisit.setConfirmed(true);
        repository.save(loadedVisit);
    }

    @Override
    public void delete(Long id) {
        Visit loadedVisit = repository
                .findById(Optional.ofNullable(id).orElseThrow(() -> new EntityNotFoundException("Wrong token")))
                .orElseThrow(() -> new EntityNotFoundException("Wrong token | Visit already canceled"));

        LocalDateTime executeTime = LocalDateTime.now();
        if(executeTime.isAfter(loadedVisit.getTime()))
            throw new VisitTimeException("Too late to cancel visit",id,executeTime,loadedVisit.getTime());

        repository.delete(loadedVisit);

    }

    @Override
    public List<Visit> findAllVisitInTime(LocalDateTime fromTime, LocalDateTime toTime) {
        return repository.findAllByTimeAfterAndTimeBeforeOrderByTime(fromTime.minusSeconds(1), toTime);
    }


}
