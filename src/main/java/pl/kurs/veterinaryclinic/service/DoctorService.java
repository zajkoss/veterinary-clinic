package pl.kurs.veterinaryclinic.service;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.kurs.veterinaryclinic.exception.EmptyIdException;
import pl.kurs.veterinaryclinic.exception.NoEmptyIdException;
import pl.kurs.veterinaryclinic.exception.NoEntityException;
import pl.kurs.veterinaryclinic.model.Doctor;
import pl.kurs.veterinaryclinic.model.enums.AnimalType;
import pl.kurs.veterinaryclinic.model.enums.DoctorType;
import pl.kurs.veterinaryclinic.repository.DoctorRepository;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class DoctorService implements IDoctorService {

    private final DoctorRepository repository;

    public DoctorService(DoctorRepository repository) {
        this.repository = repository;
    }

    @Override
    public Doctor add(Doctor doctor) {
        if (doctor == null)
            throw new NoEntityException();
        if (doctor.getId() != null)
            throw new NoEmptyIdException(doctor.getId());
        doctor.setIsActive(true);
        return repository.save(doctor);
    }

    @Override
    public Optional<Doctor> get(Long id) {
        return repository.findById(Optional.ofNullable(id).orElseThrow(() -> new EmptyIdException(id)));
    }

    @Override
    public Optional<Doctor> getActiveById(Long id) {
        return repository.findDoctorByIdAndIsActiveTrue(Optional.ofNullable(id).orElseThrow(() -> new EmptyIdException(id)));
    }

    @Override
    public Page<Doctor> getAll(Pageable pageable) {
        return repository.findAll(pageable);
    }


    @Override
    public void softDelete(Long id) {
        Doctor loadedDoctor = repository
                .findById(Optional.ofNullable(id).orElseThrow(() -> new EmptyIdException(id)))
                .orElseThrow(() -> new EntityNotFoundException(id.toString()));
        loadedDoctor.setIsActive(false);
        repository.save(loadedDoctor);
    }

    @Override
    public List<Doctor> getAllForParameters(DoctorType doctorType, AnimalType animalType) {
        List<Doctor> foundDoctors;
        Doctor searchDoctor = new Doctor();
        searchDoctor.setIsActive(true);
        if (doctorType != null)
            searchDoctor.setType(doctorType);
        if (animalType != null)
            searchDoctor.setAnimalType(animalType);
        return repository.findAll(Example.of(searchDoctor));

    }
}
