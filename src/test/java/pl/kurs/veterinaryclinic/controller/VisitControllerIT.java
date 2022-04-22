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
import java.time.LocalDateTime;
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
//TODO test na data wstecz
    @BeforeEach
    void setUp() {
        visitRepository.deleteAll();
        doctorRepository.deleteAll();
        patientRepository.deleteAll();
        doctor1 = doctorRepository.save(new Doctor("Tomasz", "Kubica", new BigDecimal("30.0"), "1234567893", true, DoctorType.EYE_DOCTOR, AnimalType.DOG));
        doctor2 = doctorRepository.save(new Doctor("Katarzyna", "Lewandowska", new BigDecimal("56.0"), "1234567894", true, DoctorType.DENTIST, AnimalType.HORSE));
        doctor3 = doctorRepository.save(new Doctor("Robert", "Kubica", new BigDecimal("30.0"), "1234767893", true, DoctorType.EYE_DOCTOR, AnimalType.HORSE));
        doctor4 = doctorRepository.save(new Doctor("Robert", "Lewandowski", new BigDecimal("56.0"), "1239567894", true, DoctorType.DENTIST, AnimalType.DOG));
        patient1 = patientRepository.save(new Patient("Szarik", "Pies", "Owczarek", 1, "Tomasz", "Paluch", "lukz1184@gmail.com"));
        patient2 = patientRepository.save(new Patient("Filemon", "Kot", "Dachowiec", 5, "Dorota", "Mieszko", "lukz1184@gmail.com"));
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
                .andExpect(jsonPath("$.errorMessages", hasItem("Property: checkNearestVisit.type; value: 'eyedoctor'; message: Invalid value for: type")))
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
                .andExpect(jsonPath("$.errorMessages", hasItem("Property: checkNearestVisit.animal; value: 'dogs'; message: Invalid value for: animal")))
                .andExpect(jsonPath("$.exceptionTypeName").value("ConstraintViolationException"))
                .andExpect(jsonPath("$.errorCode").value("BAD_REQUEST"));
    }


    @Test
    public void shouldResponseBadRequestCodeWhenTryCheckVisitsWithIncorrectFromDate() throws Exception {
        //when
        mockMvc.perform(post("/visit/check")
                        .param("from", "202205-10 08:00:00"))
                //then
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorMessages").isArray())
                .andExpect(jsonPath("$.errorMessages", hasSize(1)))
                .andExpect(jsonPath("$.errorMessages", hasItem("Method argument mismatch field: from")))
                .andExpect(jsonPath("$.exceptionTypeName").value("MethodArgumentTypeMismatchException"))
                .andExpect(jsonPath("$.errorCode").value("BAD_REQUEST"));
    }

    @Test
    public void shouldResponseBadRequestCodeWhenTryCheckVisitsWithIncorrectToDate() throws Exception {
        //when
        mockMvc.perform(post("/visit/check")
                        .param("to", "202205-10 08:00:00"))
                //then
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorMessages").isArray())
                .andExpect(jsonPath("$.errorMessages", hasSize(1)))
                .andExpect(jsonPath("$.errorMessages", hasItem("Method argument mismatch field: to")))
                .andExpect(jsonPath("$.exceptionTypeName").value("MethodArgumentTypeMismatchException"))
                .andExpect(jsonPath("$.errorCode").value("BAD_REQUEST"));
    }


    @Test
    public void shouldResponseBadRequestCodeWhenTryAddVisitWithout() throws Exception {
        //when
        mockMvc.perform(post("/visit/check")
                        .param("to", "202205-10 08:00:00"))
                //then
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorMessages").isArray())
                .andExpect(jsonPath("$.errorMessages", hasSize(1)))
                .andExpect(jsonPath("$.errorMessages", hasItem("Method argument mismatch field: to")))
                .andExpect(jsonPath("$.exceptionTypeName").value("MethodArgumentTypeMismatchException"))
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
                .andExpect(jsonPath("$.errorMessages", hasItem("Doctor already has visit that date")))
                .andExpect(jsonPath("$.exceptionTypeName").value("VisitMemberException"))
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
                .andExpect(jsonPath("$.errorMessages", hasItem("Patient already has visit that date")))
                .andExpect(jsonPath("$.exceptionTypeName").value("VisitMemberException"))
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
                .andExpect(jsonPath("$.errorMessages", hasItem("Visit can only be scheduled on full hour, visitTime=2022-05-10T17:15")))
                .andExpect(jsonPath("$.exceptionTypeName").value("VisitTimeException"))
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
                .andExpect(jsonPath("$.errorMessages", hasItem("Visit can only be scheduled between 8 a.m and 8 p.m, visitTime=2022-05-10T01:00")))
                .andExpect(jsonPath("$.exceptionTypeName").value("VisitTimeException"))
                .andExpect(jsonPath("$.errorCode").value("BAD_REQUEST"));
    }



}