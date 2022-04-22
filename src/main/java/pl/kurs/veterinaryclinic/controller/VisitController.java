package pl.kurs.veterinaryclinic.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.type.ShortType;
import org.modelmapper.ModelMapper;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.kurs.veterinaryclinic.commands.CreateVisitCommand;
import pl.kurs.veterinaryclinic.dto.*;
import pl.kurs.veterinaryclinic.exception.NotFoundRelationException;
import pl.kurs.veterinaryclinic.model.Doctor;
import pl.kurs.veterinaryclinic.model.Patient;
import pl.kurs.veterinaryclinic.model.Visit;
import pl.kurs.veterinaryclinic.model.enums.AnimalType;
import pl.kurs.veterinaryclinic.model.enums.DoctorType;
import pl.kurs.veterinaryclinic.service.IDoctorService;
import pl.kurs.veterinaryclinic.service.IPatientService;
import pl.kurs.veterinaryclinic.service.IVisitService;
import pl.kurs.veterinaryclinic.validators.EnumsValidator;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/visit")
@Validated
public class VisitController {

    private IVisitService visitService;
    private IPatientService patientService;
    private IDoctorService doctorService;
    private ModelMapper mapper;

    public VisitController(IVisitService visitService, IPatientService patientService, IDoctorService doctorService, ModelMapper mapper) {
        this.visitService = visitService;
        this.patientService = patientService;
        this.doctorService = doctorService;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity<CreatedEntityDto> addVisit(@RequestBody @Valid CreateVisitCommand createVisitCommand) {
        Doctor loadDoctor = doctorService.getActiveById(createVisitCommand.getDoctorIdentity()).orElseThrow(
                () -> new NotFoundRelationException("Doctor for provided id not found",createVisitCommand.getDoctorIdentity())
        );
        Patient loadPatient = patientService.get(createVisitCommand.getPatientIdentity()).orElseThrow(
                () -> new NotFoundRelationException("Patient for provided id not found",createVisitCommand.getPatientIdentity())
        );

        Visit visit = mapper.map(createVisitCommand, Visit.class);
        visit.setDoctor(loadDoctor);
        visit.setPatient(loadPatient);
        return ResponseEntity.ok().body(
                new CreatedEntityDto(visitService.add(visit).getId())
        );
    }

    @GetMapping("/confirm/{token}")
    public ResponseEntity<ConfirmRequestWithMessageDto> confirmVisit(@PathVariable("token") Long token) {
        visitService.confirm(token);
        return ResponseEntity.ok().body(new ConfirmRequestWithMessageDto("Visit confirmed"));
    }

    @GetMapping("/cancel/{token}")
    public ResponseEntity<ConfirmRequestWithMessageDto> cancelVisit(@PathVariable("token") Long token) {
        visitService.delete(token);
        return ResponseEntity.ok().body(new ConfirmRequestWithMessageDto("Visit canceled"));
    }

    @PostMapping("/check")
    public ResponseEntity<List<AvailableVisitDto>> checkNearestVisit(
            @RequestParam(required = false, name = "type") @EnumsValidator(enumClass = DoctorType.class,message = "Invalid value for: type") String type,
            @RequestParam(required = false, name = "animal") @EnumsValidator(enumClass = AnimalType.class,message = "Invalid value for: animal") String animal,
            @RequestParam(required = false, name = "from")  LocalDateTime fromTime,
            @RequestParam(required = false, name = "to")  LocalDateTime toTime
    ) {
        DoctorType doctorType = type == null ? null : DoctorType.valueOf(type.toUpperCase());
        AnimalType animalType = animal == null ? null : AnimalType.valueOf(animal.toUpperCase());
        List<Doctor> foundDoctors = doctorService.getAllForParameters(doctorType,animalType);

        if(fromTime == null) fromTime = LocalDateTime.now();
        if(toTime == null) toTime = fromTime.plusDays(2);
        List<AvailableVisitDto> plannedVisit = visitService.findAllVisitInTime(fromTime, toTime).stream().map(this::mapVisitToAvailableVisitDto).collect(Collectors.toList());

        List<AvailableVisitDto> result = new ArrayList<>();
        LocalDateTime startTime = fromTime;
        if(startTime.getMinute() != 0 || startTime.getSecond() != 0)
            startTime = startTime.withMinute(0).withSecond(0).plusHours(1);

        while (startTime.isBefore(toTime)) {
            LocalDateTime finalStartTime = startTime;
            if(finalStartTime.getHour() >= 8 && finalStartTime.getHour() <= 20) {
                result.addAll(
                        foundDoctors.stream()
                                .map(doctor -> mapDoctorToAvailableVisitDto(doctor, finalStartTime))
                                .filter(availableVisitDto -> !plannedVisit.contains(availableVisitDto))
                                .collect(Collectors.toList())
                );
            }
            startTime = startTime.plusHours(1);
        }
        return ResponseEntity.ok().body(result.stream().limit(10).collect(Collectors.toList()));

    }

    private AvailableVisitDto mapVisitToAvailableVisitDto(Visit visit) {
        return new AvailableVisitDto(
                new DoctorNameDto(visit.getDoctor().getId(), visit.getDoctor().getName() + " " + visit.getDoctor().getSurname()),
                visit.getTime()
        );
    }

    private AvailableVisitDto mapDoctorToAvailableVisitDto(Doctor doctor, LocalDateTime hour) {
        return new AvailableVisitDto(
                new DoctorNameDto(doctor.getId(), doctor.getName() + " " + doctor.getSurname()),
                hour
        );
    }
}

