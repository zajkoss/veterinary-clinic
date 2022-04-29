package pl.kurs.veterinaryclinic.controller;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.kurs.veterinaryclinic.commands.CreateDoctorCommand;
import pl.kurs.veterinaryclinic.dto.DoctorDto;
import pl.kurs.veterinaryclinic.dto.StatusDto;
import pl.kurs.veterinaryclinic.model.Doctor;
import pl.kurs.veterinaryclinic.service.DoctorService;
import pl.kurs.veterinaryclinic.service.IDoctorService;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/doctor")
@Validated
public class DoctorController {

    private IDoctorService doctorService;
    private ModelMapper mapper;

    public DoctorController(DoctorService doctorService, ModelMapper mapper) {
        this.doctorService = doctorService;
        this.mapper = mapper;
    }

    @GetMapping()
    public ResponseEntity<List<DoctorDto>> getAll(
            @PageableDefault Pageable pageable
    ) {
        Page<Doctor> loadedDoctorPage = doctorService.getAll(pageable);
        return ResponseEntity.ok(
                loadedDoctorPage
                        .getContent()
                        .stream()
                        .map(doctor -> mapper.map(doctor, DoctorDto.class))
                        .collect(Collectors.toList())

        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<DoctorDto> getDoctorById(@PathVariable("id") long id) {

        return ResponseEntity.ok(
                mapper.map(doctorService.get(id).orElseThrow(
                        () -> new EntityNotFoundException(Long.toString(id))
                ), DoctorDto.class)
        );
    }

    @PostMapping
    public ResponseEntity<DoctorDto> addDoctor(@RequestBody @Valid CreateDoctorCommand doctor) {
        Doctor newDoctor = mapper.map(doctor, Doctor.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.map(doctorService.add(newDoctor), DoctorDto.class));
    }

    @PutMapping("/fire/{id}")
    public ResponseEntity<StatusDto> softDeleteDoctor(@PathVariable("id") long id) {
        doctorService.softDelete(id);
        return ResponseEntity.ok().body(new StatusDto(Long.toString(id)));
    }


}
