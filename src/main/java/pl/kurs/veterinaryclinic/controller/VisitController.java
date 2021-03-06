package pl.kurs.veterinaryclinic.controller;

import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.kurs.veterinaryclinic.commands.CreateVisitCommand;
import pl.kurs.veterinaryclinic.commands.QueryAvailableVisitCommand;
import pl.kurs.veterinaryclinic.dto.AvailableVisitDto;
import pl.kurs.veterinaryclinic.dto.ConfirmRequestWithMessageDto;
import pl.kurs.veterinaryclinic.dto.CreatedEntityDto;
import pl.kurs.veterinaryclinic.dto.DoctorNameDto;
import pl.kurs.veterinaryclinic.exception.NotFoundRelationException;
import pl.kurs.veterinaryclinic.model.Doctor;
import pl.kurs.veterinaryclinic.model.Patient;
import pl.kurs.veterinaryclinic.model.Visit;
import pl.kurs.veterinaryclinic.model.enums.AnimalType;
import pl.kurs.veterinaryclinic.model.enums.DoctorType;
import pl.kurs.veterinaryclinic.service.IDoctorService;
import pl.kurs.veterinaryclinic.service.IPatientService;
import pl.kurs.veterinaryclinic.service.IVisitService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/visit")
@Validated
public class VisitController {

    private final IVisitService visitService;
    private final IPatientService patientService;
    private final IDoctorService doctorService;
    private final ModelMapper mapper;
    private final ApplicationEventPublisher applicationEventPublisher;

    public VisitController(IVisitService visitService, IPatientService patientService, IDoctorService doctorService, ModelMapper mapper, ApplicationEventPublisher applicationEventPublisher) {
        this.visitService = visitService;
        this.patientService = patientService;
        this.doctorService = doctorService;
        this.mapper = mapper;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @PostMapping
    public ResponseEntity<CreatedEntityDto> addVisit(
            @RequestBody @Valid CreateVisitCommand createVisitCommand,
            HttpServletRequest request
    ) {
        Doctor loadDoctor = doctorService.getActiveById(createVisitCommand.getDoctorIdentity()).orElseThrow(
                () -> new NotFoundRelationException("Doctor for provided id not found", createVisitCommand.getDoctorIdentity())
        );
        Patient loadPatient = patientService.get(createVisitCommand.getPatientIdentity()).orElseThrow(
                () -> new NotFoundRelationException("Patient for provided id not found", createVisitCommand.getPatientIdentity())
        );

        Visit visit = mapper.map(createVisitCommand, Visit.class);
        visit.setDoctor(loadDoctor);
        visit.setPatient(loadPatient);
        Visit createdVisit = visitService.add(visit);

//        applicationEventPublisher.publishEvent(new OnMakeVisitEvent(createdVisit,request.getRequestURL().toString().replace("/visit","")));

        return ResponseEntity.ok().body(new CreatedEntityDto(createdVisit.getId()));
    }

    @GetMapping("/confirm/{token}")
    public ResponseEntity<ConfirmRequestWithMessageDto> confirmVisit(@PathVariable("token") String token) {
        visitService.confirm(token);
        return ResponseEntity.ok().body(new ConfirmRequestWithMessageDto("Visit confirmed"));
    }

    @GetMapping("/cancel/{token}")
    public ResponseEntity<ConfirmRequestWithMessageDto> cancelVisit(@PathVariable("token") String token) {
        visitService.delete(token);
        return ResponseEntity.ok().body(new ConfirmRequestWithMessageDto("Visit canceled"));
    }


    @PostMapping("/check")
    public ResponseEntity<List<AvailableVisitDto>> checkNearestVisit(
            @ModelAttribute @Valid QueryAvailableVisitCommand queryAvailableVisitCommand, BindingResult bindingResult
    ) {
        DoctorType doctorType = queryAvailableVisitCommand.getType() == null ? null : DoctorType.valueOf(queryAvailableVisitCommand.getType().toUpperCase());
        AnimalType animalType = queryAvailableVisitCommand.getAnimal() == null ? null : AnimalType.valueOf(queryAvailableVisitCommand.getAnimal().toUpperCase());

        if (queryAvailableVisitCommand.getFrom() == null)
            queryAvailableVisitCommand.setFrom(LocalDateTime.now());
        if (queryAvailableVisitCommand.getTo() == null)
            queryAvailableVisitCommand.setTo(queryAvailableVisitCommand.getFrom().plusDays(2));

        List<AvailableVisitDto> availableVisit = visitService.findAllAvailableVisitInTimeByDoctorTypeAndAnimal(queryAvailableVisitCommand.getFrom(), queryAvailableVisitCommand.getTo(), doctorType, animalType)
                .stream()
                .map(this::mapVisitToAvailableVisitDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok().body(availableVisit.stream().limit(10).collect(Collectors.toList()));

    }

    private AvailableVisitDto mapVisitToAvailableVisitDto(Visit visit) {
        return new AvailableVisitDto(
                new DoctorNameDto(visit.getDoctor().getId(), visit.getDoctor().getName() + " " + visit.getDoctor().getSurname()),
                visit.getTime()
        );
    }
}

