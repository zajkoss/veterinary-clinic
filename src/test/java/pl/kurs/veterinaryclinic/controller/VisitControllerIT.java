package pl.kurs.veterinaryclinic.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import pl.kurs.veterinaryclinic.VeterinaryClinicApplication;
import pl.kurs.veterinaryclinic.commands.CreateVisitCommand;
import pl.kurs.veterinaryclinic.dto.AvailableVisitDto;
import pl.kurs.veterinaryclinic.model.Doctor;
import pl.kurs.veterinaryclinic.model.Patient;
import pl.kurs.veterinaryclinic.model.Visit;
import pl.kurs.veterinaryclinic.model.enums.AnimalType;
import pl.kurs.veterinaryclinic.model.enums.DoctorType;
import pl.kurs.veterinaryclinic.repository.DoctorRepository;
import pl.kurs.veterinaryclinic.repository.PatientRepository;
import pl.kurs.veterinaryclinic.repository.VisitRepository;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = VeterinaryClinicApplication.class)
@AutoConfigureMockMvc
class VisitControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private VisitRepository visitRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private PatientRepository patientRepository;

    private Doctor doctor1;
    private Doctor doctor2;
    private Doctor doctor3;
    private Doctor doctor4;
    private Patient patient1;
    private Patient patient2;
    private Visit visit1;
    private Visit visit2;
    private Visit visit3;
    private Visit visit4;

    @BeforeEach
    void setUp() {
        visitRepository.deleteAll();
        doctorRepository.deleteAll();
        patientRepository.deleteAll();
        doctor1 = doctorRepository.save(new Doctor("Tomasz", "Kubica", new BigDecimal("30.0"), "1234567893", true, DoctorType.EYE_DOCTOR, AnimalType.DOG,new HashSet<>()));
        doctor2 = doctorRepository.save(new Doctor("Katarzyna", "Lewandowska", new BigDecimal("56.0"), "1234567894", true, DoctorType.DENTIST, AnimalType.HORSE,new HashSet<>()));
        doctor3 = doctorRepository.save(new Doctor("Robert", "Kubica", new BigDecimal("30.0"), "1234767893", true, DoctorType.EYE_DOCTOR, AnimalType.HORSE,new HashSet<>()));
        doctor4 = doctorRepository.save(new Doctor("Robert", "Lewandowski", new BigDecimal("56.0"), "1239567894", true, DoctorType.DENTIST, AnimalType.DOG,new HashSet<>()));
        patient1 = patientRepository.save(new Patient("Szarik", "Pies", "Owczarek", 1, "Tomasz", "Paluch", "lukz1184@gmail.com",new HashSet<>()));
        patient2 = patientRepository.save(new Patient("Filemon", "Kot", "Dachowiec", 5, "Dorota", "Mieszko", "lukz1184@gmail.com",new HashSet<>()));
        visit1 = visitRepository.save(new Visit(doctor1, patient1, LocalDateTime.of(2022, 5, 10, 10, 0)));
        visit2 = visitRepository.save(new Visit(doctor1, patient2, LocalDateTime.of(2022, 5, 10, 11, 0)));
        visit3 = visitRepository.save(new Visit(doctor2, patient1, LocalDateTime.of(2022, 5, 10, 12, 0)));
        visit4 = visitRepository.save(new Visit(doctor2, patient2, LocalDateTime.of(2022, 5, 10, 13, 0)));
    }

    @Test
    public void shouldFindFirst10AvailableVisit10May2022PerHour() throws Exception {
        String responseJson = mockMvc.perform(post("/visit/check")
                        .param("from", "2022-05-10 10:00:00"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        //then
        List<AvailableVisitDto> visitsResponse = objectMapper.readValue(responseJson, new TypeReference<List<AvailableVisitDto>>() {
        });
        assertEquals(10, visitsResponse.size());
        visitsResponse.forEach(visit ->
                assertTrue(visit.getDate().getMinute() == 0 || visit.getDate().getSecond() == 0)
        );

    }

    @Test
    public void shouldFindFirst6AvailableVisit10May2022PerHour() throws Exception {
        String responseJson = mockMvc.perform(post("/visit/check")
                        .param("from", "2022-05-10 10:00:00")
                        .param("to", "2022-05-10 12:00:00")
                )
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        //then
        List<AvailableVisitDto> visitsResponse = objectMapper.readValue(responseJson, new TypeReference<List<AvailableVisitDto>>() {
        });
        assertEquals(6, visitsResponse.size());
        visitsResponse.forEach(visit ->
                assertTrue(visit.getDate().getMinute() == 0 || visit.getDate().getSecond() == 0)
        );

    }


    @Test
    public void shouldFindFirst10AvailableVisit10May2022FilterEyeDoctor() throws Exception {
        String responseJson = mockMvc.perform(post("/visit/check")
                        .param("from", "2022-05-10 07:00:00")
                        .param("type", "eye_doctor"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        //then
        List<AvailableVisitDto> visitsResponse = objectMapper.readValue(responseJson, new TypeReference<List<AvailableVisitDto>>() {
        });
        assertEquals(10, visitsResponse.size());
        List<Doctor> doctors = List.of(doctor1, doctor2, doctor3, doctor4);
        visitsResponse.forEach(visit -> {
                    Optional<Doctor> doctor = doctors.stream().filter(d -> d.getId() == visit.getDoctor().getId()).findFirst();
                    assertSame(doctor.get().getType(), DoctorType.EYE_DOCTOR);
                }
        );
    }

    @Test
    public void shouldFindFirst10AvailableVisit10May2022FilterEyeDoctorAndDogDoctor() throws Exception {
        String responseJson = mockMvc.perform(post("/visit/check")
                        .param("from", "2022-05-10 07:00:00")
                        .param("type", "eye_doctor")
                        .param("animal", "dog")
                ).andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        //then
        List<AvailableVisitDto> visitsResponse = objectMapper.readValue(responseJson, new TypeReference<List<AvailableVisitDto>>() {
        });
        assertEquals(10, visitsResponse.size());
        List<Doctor> doctors = List.of(doctor1, doctor2, doctor3, doctor4);
        visitsResponse.forEach(visit -> {
                    Optional<Doctor> doctor = doctors.stream().filter(d -> d.getId() == visit.getDoctor().getId()).findFirst();
                    assertSame(doctor.get().getAnimalType(), AnimalType.DOG);
                    assertSame(doctor.get().getType(), DoctorType.EYE_DOCTOR);
                }
        );
    }

    @Test
    public void shouldFindFirst10AvailableVisit10May2022FilterDogDoctor() throws Exception {
        String responseJson = mockMvc.perform(post("/visit/check")
                        .param("from", "2022-05-10 07:00:00")
                        .param("animal", "dog")
                ).andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        //then
        List<AvailableVisitDto> visitsResponse = objectMapper.readValue(responseJson, new TypeReference<List<AvailableVisitDto>>() {
        });
        assertEquals(10, visitsResponse.size());
        List<Doctor> doctors = List.of(doctor1, doctor2, doctor3, doctor4);
        visitsResponse.forEach(visit -> {
                    Optional<Doctor> doctor = doctors.stream().filter(d -> d.getId() == visit.getDoctor().getId()).findFirst();
                    assertSame(doctor.get().getAnimalType(), AnimalType.DOG);
                }
        );
    }

    @Test
    public void shouldResponseBadRequestCodeWhenTryCheckVisitsWithIncorrectDoctorType() throws Exception {
        //when
        mockMvc.perform(post("/visit/check")
                        .param("from", "2022-05-10 08:00:00")
                        .param("type", "eyedoctor"))
                //then
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorMessages").isArray())
                .andExpect(jsonPath("$.errorMessages", hasSize(1)))
                .andExpect(jsonPath("$.errorMessages", hasItem("Property: checkNearestVisit.queryAvailableVisitCommand.type; value: 'eyedoctor'; message: Invalid value for: type")))
                .andExpect(jsonPath("$.exceptionTypeName").value("ConstraintViolationException"))
                .andExpect(jsonPath("$.errorCode").value("BAD_REQUEST"));
    }

    @Test
    public void shouldResponseBadRequestCodeWhenTryCheckVisitsWithIncorrectAnimalType() throws Exception {
        //when
        mockMvc.perform(post("/visit/check")
                        .param("from", "2022-05-10 08:00:00")
                        .param("animal", "dogs"))
                //then
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorMessages").isArray())
                .andExpect(jsonPath("$.errorMessages", hasSize(1)))
                .andExpect(jsonPath("$.errorMessages", hasItem("Property: checkNearestVisit.queryAvailableVisitCommand.animal; value: 'dogs'; message: Invalid value for: animal")))
                .andExpect(jsonPath("$.exceptionTypeName").value("ConstraintViolationException"))
                .andExpect(jsonPath("$.errorCode").value("BAD_REQUEST"));
    }


    @Test
    public void shouldResponseBadRequestCodeWhenTryAddVisitWithIncorrectDoctorID() throws Exception {
        CreateVisitCommand createVisitCommand = new CreateVisitCommand();
        createVisitCommand.setDoctorIdentity(999L);
        createVisitCommand.setPatientIdentity(patient1.getId());
        createVisitCommand.setTime(LocalDateTime.of(2022, 5, 10, 15, 0));
        //when
        mockMvc.perform(post("/visit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createVisitCommand)))
                //then
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorMessages").isArray())
                .andExpect(jsonPath("$.errorMessages", hasSize(1)))
                .andExpect(jsonPath("$.errorMessages", hasItem("Doctor for provided id not found, id=999")))
                .andExpect(jsonPath("$.exceptionTypeName").value("NotFoundRelationException"))
                .andExpect(jsonPath("$.errorCode").value("BAD_REQUEST"));
    }

    @Test
    public void shouldResponseBadRequestCodeWhenTryAddVisitWithEmptyDoctorID() throws Exception {
        CreateVisitCommand createVisitCommand = new CreateVisitCommand();
        createVisitCommand.setPatientIdentity(patient1.getId());
        createVisitCommand.setTime(LocalDateTime.of(2022, 5, 10, 15, 0));
        //when
        mockMvc.perform(post("/visit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createVisitCommand)))
                //then
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorMessages").isArray())
                .andExpect(jsonPath("$.errorMessages", hasSize(1)))
                .andExpect(jsonPath("$.errorMessages", hasItem("id:null")))
                .andExpect(jsonPath("$.exceptionTypeName").value("EmptyIdException"))
                .andExpect(jsonPath("$.errorCode").value("BAD_REQUEST"));
    }

    @Test
    public void shouldResponseBadRequestCodeWhenTryAddVisitWithInactiveDoctor() throws Exception {
        CreateVisitCommand createVisitCommand = new CreateVisitCommand();
        doctor4.setIsActive(false);
        doctorRepository.save(doctor4);
        createVisitCommand.setDoctorIdentity(doctor4.getId());
        createVisitCommand.setPatientIdentity(patient1.getId());
        createVisitCommand.setTime(LocalDateTime.of(2022, 5, 10, 15, 0));
        //when
        mockMvc.perform(post("/visit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createVisitCommand)))
                //then
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorMessages").isArray())
                .andExpect(jsonPath("$.errorMessages", hasSize(1)))
                .andExpect(jsonPath("$.errorMessages", hasItem("Doctor for provided id not found, id=" + doctor4.getId())))
                .andExpect(jsonPath("$.exceptionTypeName").value("NotFoundRelationException"))
                .andExpect(jsonPath("$.errorCode").value("BAD_REQUEST"));
    }


    @Test
    public void shouldResponseBadRequestCodeWhenTryAddVisitWithIncorrectPatientID() throws Exception {
        CreateVisitCommand createVisitCommand = new CreateVisitCommand();
        createVisitCommand.setDoctorIdentity(doctor1.getId());
        createVisitCommand.setPatientIdentity(999L);
        createVisitCommand.setTime(LocalDateTime.of(2022, 5, 10, 15, 0));
        //when
        mockMvc.perform(post("/visit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createVisitCommand)))
                //then
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorMessages").isArray())
                .andExpect(jsonPath("$.errorMessages", hasSize(1)))
                .andExpect(jsonPath("$.errorMessages", hasItem("Patient for provided id not found, id=999")))
                .andExpect(jsonPath("$.exceptionTypeName").value("NotFoundRelationException"))
                .andExpect(jsonPath("$.errorCode").value("BAD_REQUEST"));
    }

    @Test
    public void shouldResponseBadRequestCodeWhenTryAddVisitWithEmptyPatientID() throws Exception {
        CreateVisitCommand createVisitCommand = new CreateVisitCommand();
        createVisitCommand.setDoctorIdentity(doctor1.getId());
        createVisitCommand.setTime(LocalDateTime.of(2022, 5, 10, 15, 0));
        //when
        mockMvc.perform(post("/visit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createVisitCommand)))
                //then
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorMessages").isArray())
                .andExpect(jsonPath("$.errorMessages", hasSize(1)))
                .andExpect(jsonPath("$.errorMessages", hasItem("id:null")))
                .andExpect(jsonPath("$.exceptionTypeName").value("EmptyIdException"))
                .andExpect(jsonPath("$.errorCode").value("BAD_REQUEST"));
    }

    @Test
    public void shouldResponseBadRequestCodeWhenTryAddVisitForDoctorWhoHasAlreadyVisit() throws Exception {
        CreateVisitCommand createVisitCommand = new CreateVisitCommand();
        createVisitCommand.setDoctorIdentity(doctor1.getId());
        createVisitCommand.setPatientIdentity(patient2.getId());
        createVisitCommand.setTime(LocalDateTime.of(2022, 5, 10, 10, 0));
        //when
        mockMvc.perform(post("/visit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createVisitCommand)))
                //then
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorMessages").isArray())
                .andExpect(jsonPath("$.errorMessages", hasSize(1)))
                .andExpect(jsonPath("$.errorMessages", hasItem("Property: createVisitCommand'; message: Doctor/Patient already has visit that date")))
                .andExpect(jsonPath("$.exceptionTypeName").value("MethodArgumentNotValidException"))
                .andExpect(jsonPath("$.errorCode").value("BAD_REQUEST"));
    }


    @Test
    public void shouldResponseBadRequestCodeWhenTryAddVisitForPatientWhoHasAlreadyVisit() throws Exception {
        CreateVisitCommand createVisitCommand = new CreateVisitCommand();
        createVisitCommand.setDoctorIdentity(doctor2.getId());
        createVisitCommand.setPatientIdentity(patient1.getId());
        createVisitCommand.setTime(LocalDateTime.of(2022, 5, 10, 10, 0));
        //when
        mockMvc.perform(post("/visit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createVisitCommand)))
                //then
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorMessages").isArray())
                .andExpect(jsonPath("$.errorMessages", hasSize(1)))
                .andExpect(jsonPath("$.errorMessages", hasItem("Property: createVisitCommand'; message: Doctor/Patient already has visit that date")))
                .andExpect(jsonPath("$.exceptionTypeName").value("MethodArgumentNotValidException"))
                .andExpect(jsonPath("$.errorCode").value("BAD_REQUEST"));
    }


    @Test
    public void shouldResponseBadRequestCodeWhenTryAddVisitWithNotFullHour() throws Exception {
        CreateVisitCommand createVisitCommand = new CreateVisitCommand();
        createVisitCommand.setDoctorIdentity(doctor2.getId());
        createVisitCommand.setPatientIdentity(patient1.getId());
        createVisitCommand.setTime(LocalDateTime.of(2022, 5, 10, 17, 15));
        //when
        mockMvc.perform(post("/visit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createVisitCommand)))
                //then
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorMessages").isArray())
                .andExpect(jsonPath("$.errorMessages", hasSize(1)))
                .andExpect(jsonPath("$.errorMessages", hasItem("Property: time; value: '2022-05-10T17:15'; message: Wrong visit time")))
                .andExpect(jsonPath("$.exceptionTypeName").value("MethodArgumentNotValidException"))
                .andExpect(jsonPath("$.errorCode").value("BAD_REQUEST"));
    }


    @Test
    public void shouldResponseBadRequestCodeWhenTryAddVisitNotBetweenCorrectTime() throws Exception {
        CreateVisitCommand createVisitCommand = new CreateVisitCommand();
        createVisitCommand.setDoctorIdentity(doctor2.getId());
        createVisitCommand.setPatientIdentity(patient1.getId());
        createVisitCommand.setTime(LocalDateTime.of(2022, 5, 10, 1, 0));
        //when
        mockMvc.perform(post("/visit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createVisitCommand)))
                //then
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorMessages").isArray())
                .andExpect(jsonPath("$.errorMessages", hasSize(1)))
                .andExpect(jsonPath("$.errorMessages", hasItem("Property: time; value: '2022-05-10T01:00'; message: Wrong visit time")))
                .andExpect(jsonPath("$.exceptionTypeName").value("MethodArgumentNotValidException"))
                .andExpect(jsonPath("$.errorCode").value("BAD_REQUEST"));
    }


    @Test
    public void shouldResponseBadRequestCodeWhenTryAddVisitInPastTime() throws Exception {
        CreateVisitCommand createVisitCommand = new CreateVisitCommand();
        createVisitCommand.setDoctorIdentity(doctor2.getId());
        createVisitCommand.setPatientIdentity(patient1.getId());
        createVisitCommand.setTime(LocalDateTime.of(1992, 5, 10, 1, 0));
        //when
        mockMvc.perform(post("/visit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createVisitCommand)))
                //then
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorMessages").isArray())
                .andExpect(jsonPath("$.errorMessages", hasSize(1)))
                .andExpect(jsonPath("$.errorMessages", hasItem("Property: time; value: '1992-05-10T01:00'; message: Wrong visit time")))
                .andExpect(jsonPath("$.exceptionTypeName").value("MethodArgumentNotValidException"))
                .andExpect(jsonPath("$.errorCode").value("BAD_REQUEST"));
    }

    @Test
    public void pes() throws Exception {
        CreateVisitCommand createVisitCommand = new CreateVisitCommand();
        createVisitCommand.setDoctorIdentity(doctor2.getId());
        createVisitCommand.setPatientIdentity(patient1.getId());
        createVisitCommand.setTime(LocalDateTime.of(2022, 6, 10, 11, 0));

        CreateVisitCommand createVisitCommand2 = new CreateVisitCommand();
        createVisitCommand.setDoctorIdentity(doctor2.getId());
        createVisitCommand.setPatientIdentity(patient2.getId());
        createVisitCommand.setTime(LocalDateTime.of(2022, 6, 10, 11, 0));

        Runnable runnable1 = new Runnable() {
            @Override
            public void run() {
                try {
                    mockMvc.perform(post("/visit")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(createVisitCommand)))
                            .andExpect(status().isOk());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        Runnable runnable2 = new Runnable() {
            @Override
            public void run() {
                try {
                    mockMvc.perform(post("/visit")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(objectMapper.writeValueAsString(createVisitCommand2)))
                            .andExpect(status().isOk());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        runnable1.run();
        runnable2.run();
    }



}