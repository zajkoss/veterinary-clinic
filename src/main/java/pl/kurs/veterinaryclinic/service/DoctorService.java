package pl.kurs.veterinaryclinic.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.kurs.veterinaryclinic.exception.EmptyIdException;
import pl.kurs.veterinaryclinic.exception.NoEmptyIdException;
import pl.kurs.veterinaryclinic.exception.NoEntityException;
import pl.kurs.veterinaryclinic.model.Doctor;
import pl.kurs.veterinaryclinic.repository.DoctorRepository;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Service
public class DoctorService implements IDoctorService {

    private DoctorRepository repository;

    public DoctorService(DoctorRepository repository) {
        this.repository = repository;
    }

    @Override
    public Doctor add(Doctor doctor) {
        if (doctor == null)
            throw new NoEntityException();

        if (doctor.getId() != null)
            throw new NoEmptyIdException(doctor.getId());

        return repository.save(doctor);
    }

    @Override
    public Doctor get(Long id) {
        return repository
                .findById(Optional.ofNullable(id).orElseThrow(() -> new EmptyIdException(id)))
                .orElseThrow(() -> new EntityNotFoundException("" + id));
    }

    @Override
    public Page<Doctor> getAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public void softDelete(Long id) {
        Doctor loadedDoctor = repository
                .findById(Optional.ofNullable(id).orElseThrow(() -> new EmptyIdException(id)))
                .orElseThrow(() -> new EntityNotFoundException("" + id));
        loadedDoctor.setIsActive(false);
        repository.save(loadedDoctor);
    }
}
