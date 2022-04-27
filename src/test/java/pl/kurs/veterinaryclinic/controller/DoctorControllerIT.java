package pl.kurs.veterinaryclinic.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import pl.kurs.veterinaryclinic.VeterinaryClinicApplication;
import pl.kurs.veterinaryclinic.commands.CreateDoctorCommand;
import pl.kurs.veterinaryclinic.dto.DoctorDto;
import pl.kurs.veterinaryclinic.model.Doctor;
import pl.kurs.veterinaryclinic.model.enums.AnimalType;
import pl.kurs.veterinaryclinic.model.enums.DoctorType;
import pl.kurs.veterinaryclinic.repository.DoctorRepository;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = VeterinaryClinicApplication.class)
@AutoConfigureMockMvc
class DoctorControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private DoctorRepository doctorRepository;

    @Test
    public void shouldGetSingleDoctor() throws Exception {
        //given -
        Doctor doctorAdam = doctorRepository.save(new Doctor("Adam", "Janko", new BigDecimal("110.00"), "1114567891", true, DoctorType.CARDIOLOGIST, AnimalType.DOG,new HashSet<>()));
        //when
        String responseJson = mockMvc.perform(get("/doctor/" + doctorAdam.getId()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        //then
        Doctor doctorResponse = objectMapper.readValue(responseJson, Doctor.class);
        assertEquals(doctorResponse, doctorAdam);
    }


    @Test
    public void shouldResponseNotFoundCodeWhenTryGetNoExistSingleDoctor() throws Exception {
        mockMvc.perform(get("/doctor/" + 999))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorMessages").isArray())
                .andExpect(jsonPath("$.errorMessages", hasSize(1)))
                .andExpect(jsonPath("$.errorMessages", hasItem("999")))
                .andExpect(jsonPath("$.exceptionTypeName").value("EntityNotFoundException"))
                .andExpect(jsonPath("$.errorCode").value("NOT_FOUND"));
    }


    @Test
    public void shouldAddNewDoctor() throws Exception {
        //given
        Doctor doctor = new Doctor("Jan", "Kowalski", new BigDecimal("100.00"), "1234597890", true, DoctorType.EYE_DOCTOR, AnimalType.CAT,new HashSet<>());
        DoctorDto doctorDto = modelMapper.map(doctor, DoctorDto.class);
        String createDoctorCommandJson = objectMapper.writeValueAsString(modelMapper.map(doctor, CreateDoctorCommand.class));
        //when
        String postResponseJson = mockMvc.perform(post("/doctor")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createDoctorCommandJson))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();
        long generatedId = objectMapper.readValue(postResponseJson, DoctorDto.class).getId();
        doctorDto.setId(generatedId);
        //then
        String responseJson = mockMvc.perform(get("/doctor/" + generatedId))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        //then
        DoctorDto doctorResponseDto = objectMapper.readValue(responseJson, DoctorDto.class);
        assertEquals(doctorResponseDto, doctorDto);
    }

    @Test
    public void shouldResponseBadRequestCodeWhenTryAddNewDoctorWithoutNameAndSurname() throws Exception {
        //given
        Doctor doctor = new Doctor("", "", new BigDecimal("100.00"), "9994567890", true, DoctorType.EYE_DOCTOR, AnimalType.CAT,new HashSet<>());
        String createDoctorCommandJson = objectMapper.writeValueAsString(modelMapper.map(doctor, CreateDoctorCommand.class));
        //when
        mockMvc.perform(post("/doctor")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createDoctorCommandJson))
                //then
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorMessages").isArray())
                .andExpect(jsonPath("$.errorMessages", hasSize(2)))
                .andExpect(jsonPath("$.errorMessages", hasItem("Property: name; value: ''; message: must not be blank")))
                .andExpect(jsonPath("$.errorMessages", hasItem("Property: surname; value: ''; message: must not be blank")))
                .andExpect(jsonPath("$.exceptionTypeName").value("MethodArgumentNotValidException"))
                .andExpect(jsonPath("$.errorCode").value("BAD_REQUEST"));
    }

    @Test
    public void shouldResponseBadRequestCodeWhenTryAddNewDoctorWithoutNegativeSalary() throws Exception {
        //given
        Doctor doctor = new Doctor("Jan", "Kowalski", new BigDecimal("-10.00"), "9234517890", true, DoctorType.EYE_DOCTOR, AnimalType.CAT,new HashSet<>());
        String createDoctorCommandJson = objectMapper.writeValueAsString(modelMapper.map(doctor, CreateDoctorCommand.class));
        //when
        mockMvc.perform(post("/doctor")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createDoctorCommandJson))
                //then
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorMessages").isArray())
                .andExpect(jsonPath("$.errorMessages", hasSize(1)))
                .andExpect(jsonPath("$.errorMessages", hasItem("Property: salary; value: '-10.00'; message: must be greater than 0")))
                .andExpect(jsonPath("$.exceptionTypeName").value("MethodArgumentNotValidException"))
                .andExpect(jsonPath("$.errorCode").value("BAD_REQUEST"));
    }

    @Test
    public void shouldResponseBadRequestCodeWhenTryAddNewDoctorWithNipNumberLessThan10() throws Exception {
        //given
        Doctor doctor = new Doctor("Jan", "Kowalski", new BigDecimal("10.00"), "923456789", true, DoctorType.EYE_DOCTOR, AnimalType.CAT,new HashSet<>());
        String createDoctorCommandJson = objectMapper.writeValueAsString(modelMapper.map(doctor, CreateDoctorCommand.class));
        //when
        mockMvc.perform(post("/doctor")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createDoctorCommandJson))
                //then
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorMessages").isArray())
                .andExpect(jsonPath("$.errorMessages", hasSize(1)))
                .andExpect(jsonPath("$.errorMessages", hasItem("Property: nip; value: '923456789'; message: Wrong NIP number format")))
                .andExpect(jsonPath("$.exceptionTypeName").value("MethodArgumentNotValidException"))
                .andExpect(jsonPath("$.errorCode").value("BAD_REQUEST"));
    }

    @Test
    public void shouldResponseBadRequestCodeWhenTryAddNewDoctorWithNipNumberMoreThan10() throws Exception {
        //given
        Doctor doctor = new Doctor("Jan", "Kowalski", new BigDecimal("10.00"), "92345678901", true, DoctorType.EYE_DOCTOR, AnimalType.CAT,new HashSet<>());
        String createDoctorCommandJson = objectMapper.writeValueAsString(modelMapper.map(doctor, CreateDoctorCommand.class));
        //when
        mockMvc.perform(post("/doctor")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createDoctorCommandJson))
                //then
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorMessages").isArray())
                .andExpect(jsonPath("$.errorMessages", hasSize(1)))
                .andExpect(jsonPath("$.errorMessages", hasItem("Property: nip; value: '92345678901'; message: Wrong NIP number format")))
                .andExpect(jsonPath("$.exceptionTypeName").value("MethodArgumentNotValidException"))
                .andExpect(jsonPath("$.errorCode").value("BAD_REQUEST"));
    }

    @Test
    public void shouldResponseBadRequestCodeWhenTryAddNewDoctorWithDuplicatedNipNumber() throws Exception {
        Doctor doctor1 = new Doctor("Jan", "Kowalski", new BigDecimal("10.00"), "9234567890", true, DoctorType.EYE_DOCTOR, AnimalType.CAT,new HashSet<>());
        doctorRepository.save(doctor1);
        //given
        Doctor doctor = new Doctor("Jan", "Kowalski", new BigDecimal("10.00"), "9234567890", true, DoctorType.EYE_DOCTOR, AnimalType.CAT,new HashSet<>());
        String createDoctorCommandJson = objectMapper.writeValueAsString(modelMapper.map(doctor, CreateDoctorCommand.class));
        //when
        mockMvc.perform(post("/doctor")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createDoctorCommandJson))
                //then
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorMessages").isArray())
                .andExpect(jsonPath("$.errorMessages", hasSize(1)))
                .andExpect(jsonPath("$.errorMessages", hasItem("Property: nip; value: '9234567890'; message: NIP number not unique")))
                .andExpect(jsonPath("$.exceptionTypeName").value("MethodArgumentNotValidException"))
                .andExpect(jsonPath("$.errorCode").value("BAD_REQUEST"));
    }

    @Test
    public void shouldResponseBadRequestCodeWhenTryAddNewDoctorWithIncorrectDoctorTypeAndIncorrectAnimalType() throws Exception {
        //given
        Doctor doctor = new Doctor("Jan", "Kowalski", new BigDecimal("10.00"), "3234567890", true, DoctorType.EYE_DOCTOR, AnimalType.CAT,new HashSet<>());
        CreateDoctorCommand createDoctorCommand = modelMapper.map(doctor, CreateDoctorCommand.class);
        createDoctorCommand.setType("ORTOPEDA");
        createDoctorCommand.setAnimalType("HIPOPOTAM");
        String createDoctorCommandJson = objectMapper.writeValueAsString(createDoctorCommand);

        //when
        mockMvc.perform(post("/doctor")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createDoctorCommandJson))
                //then
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorMessages").isArray())
                .andExpect(jsonPath("$.errorMessages", hasSize(2)))
                .andExpect(jsonPath("$.errorMessages", hasItem("Property: type; value: 'ORTOPEDA'; message: Invalid value for: type field")))
                .andExpect(jsonPath("$.errorMessages", hasItem("Property: animalType; value: 'HIPOPOTAM'; message: Invalid value for: animalType field")))
                .andExpect(jsonPath("$.exceptionTypeName").value("MethodArgumentNotValidException"))
                .andExpect(jsonPath("$.errorCode").value("BAD_REQUEST"));
    }

    @Test
    public void shouldFindFirstFiveDoctorsOrderAscendingById() throws Exception {
        //given
        List<Doctor> doctors = Arrays.asList(
                new Doctor("Katarzyna", "Lewandowska", new BigDecimal("56.00"), "2234567894", true, DoctorType.DENTIST, AnimalType.HORSE,new HashSet<>()),
                new Doctor("Anna", "Kowalczyk", new BigDecimal("44.00"), "2334567895", true, DoctorType.SURGEON, AnimalType.DOG,new HashSet<>()),
                new Doctor("Anna", "Kowalczyk", new BigDecimal("44.00"), "2434567895", true, DoctorType.SURGEON, AnimalType.DOG,new HashSet<>()),
                new Doctor("Anna", "Kowalczyk", new BigDecimal("44.00"), "2534567895", true, DoctorType.SURGEON, AnimalType.DOG,new HashSet<>()),
                new Doctor("Anna", "Kowalczyk", new BigDecimal("44.00"), "2634567895", true, DoctorType.SURGEON, AnimalType.DOG,new HashSet<>()),
                new Doctor("Anna", "Kowalczyk", new BigDecimal("44.00"), "2734567895", true, DoctorType.SURGEON, AnimalType.DOG,new HashSet<>())
        );
        doctorRepository.saveAll(doctors);

        String responseJson = mockMvc.perform(get("/doctor/")
                        .param("size","5")
                        .param("page","0"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        //then
        List<Doctor> doctorsResponse = objectMapper.readValue(responseJson, new TypeReference<List<Doctor>>() {});
        assertEquals(5, doctorsResponse.size());
        for (int i = 0; i < doctorsResponse.size(); i++) {
            if(i != 0 )
                assertTrue(doctorsResponse.get(i).getId() > doctorsResponse.get(i-1).getId());
        }
    }


    @Test
    public void shouldSoftDeleteDoctor() throws Exception {
        //given
        Doctor doctor = doctorRepository.save(new Doctor("Tomasz", "Kubica", new BigDecimal("30.00"), "1234561193", true, DoctorType.EYE_DOCTOR, AnimalType.DOG,new HashSet<>()));

        //when
        mockMvc.perform(put("/doctor/fire/" + doctor.getId()))
                .andExpect(status().isOk());

        //then
        String responseJson = mockMvc.perform(get("/doctor/" + doctor.getId()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        doctor.setIsActive(false);

        Doctor doctorResponse = objectMapper.readValue(responseJson, Doctor.class);
        assertEquals(doctorResponse, doctor);
    }


    @Test
    public void shouldResponseNotFoundCodeWhenTryFireNoExistSingleDoctor() throws Exception {
        mockMvc.perform(put("/doctor/fire/" + 999))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorMessages").isArray())
                .andExpect(jsonPath("$.errorMessages", hasSize(1)))
                .andExpect(jsonPath("$.errorMessages", hasItem("999")))
                .andExpect(jsonPath("$.exceptionTypeName").value("EntityNotFoundException"))
                .andExpect(jsonPath("$.errorCode").value("NOT_FOUND"));
    }

}