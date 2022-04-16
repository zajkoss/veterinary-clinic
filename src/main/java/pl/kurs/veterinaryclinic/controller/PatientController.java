package pl.kurs.veterinaryclinic.controller;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.kurs.veterinaryclinic.commands.CreatePatientCommand;
import pl.kurs.veterinaryclinic.dto.DoctorDto;
import pl.kurs.veterinaryclinic.dto.PatientDto;
import pl.kurs.veterinaryclinic.model.Patient;
import pl.kurs.veterinaryclinic.service.IPatientService;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/patient")
@Validated
public class PatientController {

    private IPatientService patientService;
    private ModelMapper mapper;

    public PatientController(IPatientService patientService, ModelMapper mapper) {
        this.patientService = patientService;
        this.mapper = mapper;
    }

    @GetMapping()
    public ResponseEntity<List<PatientDto>> getAll(
            @PageableDefault Pageable pageable
    ) {
        Page<Patient> loadedPatientPage = patientService.getAll(pageable);
        return ResponseEntity.ok(
                loadedPatientPage
                        .getContent()
                        .stream()
                        .map(patient -> mapper.map(patient, PatientDto.class))
                        .collect(Collectors.toList())

        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<PatientDto> getPatientById(@PathVariable("id") long id) {
        return ResponseEntity.ok(
                mapper.map(patientService.get(id).orElseThrow(
                        () -> new EntityNotFoundException("" + id)
                ), PatientDto.class)
        );
    }

    @PostMapping
    public ResponseEntity<PatientDto> addPatient(@RequestBody @Valid CreatePatientCommand patient) {
        Patient newPatient = mapper.map(patient, Patient.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.map(patientService.add(newPatient), PatientDto.class));
    }

}
