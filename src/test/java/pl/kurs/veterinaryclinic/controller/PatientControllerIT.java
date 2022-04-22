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
import pl.kurs.veterinaryclinic.commands.CreatePatientCommand;
import pl.kurs.veterinaryclinic.dto.PatientDto;
import pl.kurs.veterinaryclinic.model.Patient;
import pl.kurs.veterinaryclinic.repository.PatientRepository;


import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(classes = VeterinaryClinicApplication.class)
@AutoConfigureMockMvc
class PatientControllerIT {


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PatientRepository patientRepository;

    @Test
    public void shouldGetSinglePatient() throws Exception {
        //given -
        Patient patient = new Patient("Szarik", "Pies", "Owczarek", 1, "Tomasz", "Paluch", "a@a.com");
        patient = patientRepository.save(patient);
        //when
        String responseJson = mockMvc.perform(get("/patient/" + patient.getId()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        //then
        Patient patientResponse = objectMapper.readValue(responseJson, Patient.class);
        assertEquals(patientResponse, patient);
    }

    @Test
    public void shouldResponseNotFoundCodeWhenTryGetNoExistPatient() throws Exception {
        mockMvc.perform(get("/patient/" + 999))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorMessages").isArray())
                .andExpect(jsonPath("$.errorMessages", hasSize(1)))
                .andExpect(jsonPath("$.errorMessages", hasItem("999")))
                .andExpect(jsonPath("$.exceptionTypeName").value("EntityNotFoundException"))
                .andExpect(jsonPath("$.errorCode").value("NOT_FOUND"));
    }

    @Test
    public void shouldAddNewPatient() throws Exception {
        //given
        Patient patient = new Patient("Filemon", "Kot", "Dachowiec", 5, "Dorota", "Mieszko", "b@a.com");
        PatientDto patientDto = modelMapper.map(patient, PatientDto.class);
        String createPatientCommandJson = objectMapper.writeValueAsString(modelMapper.map(patient, CreatePatientCommand.class));
        //when
        String postResponseJson = mockMvc.perform(post("/patient")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createPatientCommandJson))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();
        long generatedId = objectMapper.readValue(postResponseJson, PatientDto.class).getId();
        patientDto.setId(generatedId);
        //then
        String responseJson = mockMvc.perform(get("/patient/" + generatedId))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        PatientDto patientResponseDto = objectMapper.readValue(responseJson, PatientDto.class);
        assertEquals(patientResponseDto, patientDto);
    }


    @Test
    public void shouldResponseBadRequestCodeWhenTryAddNewPatientWithoutName() throws Exception {
        //given
        Patient patient = new Patient("", "Kon", "Czystej krwi arabskiej", 7, "Janina", "Dudu", "c@a.com");
        String createPatientCommandJson = objectMapper.writeValueAsString(modelMapper.map(patient, CreatePatientCommand.class));
        //when
        mockMvc.perform(post("/patient")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createPatientCommandJson))
                //then
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorMessages").isArray())
                .andExpect(jsonPath("$.errorMessages", hasSize(1)))
                .andExpect(jsonPath("$.errorMessages", hasItem("Property: name; value: ''; message: must not be blank")))
                .andExpect(jsonPath("$.exceptionTypeName").value("MethodArgumentNotValidException"))
                .andExpect(jsonPath("$.errorCode").value("BAD_REQUEST"));
    }

    @Test
    public void shouldResponseBadRequestCodeWhenTryAddNewPatientWithoutSpecies() throws Exception {
        //given
        Patient patient = new Patient("Felix", "", "Czystej krwi arabskiej", 7, "Janina", "Dudu", "c@a.com");
        String createPatientCommandJson = objectMapper.writeValueAsString(modelMapper.map(patient, CreatePatientCommand.class));
        //when
        mockMvc.perform(post("/patient")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createPatientCommandJson))
                //then
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorMessages").isArray())
                .andExpect(jsonPath("$.errorMessages", hasSize(1)))
                .andExpect(jsonPath("$.errorMessages", hasItem("Property: species; value: ''; message: must not be blank")))
                .andExpect(jsonPath("$.exceptionTypeName").value("MethodArgumentNotValidException"))
                .andExpect(jsonPath("$.errorCode").value("BAD_REQUEST"));
    }

    @Test
    public void shouldResponseBadRequestCodeWhenTryAddNewPatientWithNegativeAge() throws Exception {
        //given
        Patient patient = new Patient("Felix", "Kon", "Czystej krwi arabskiej", -7, "Janina", "Dudu", "c@a.com");
        String createPatientCommandJson = objectMapper.writeValueAsString(modelMapper.map(patient, CreatePatientCommand.class));
        //when
        mockMvc.perform(post("/patient")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createPatientCommandJson))
                //then
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorMessages").isArray())
                .andExpect(jsonPath("$.errorMessages", hasSize(1)))
                .andExpect(jsonPath("$.errorMessages", hasItem("Property: age; value: '-7'; message: must be greater than or equal to 0")))
                .andExpect(jsonPath("$.exceptionTypeName").value("MethodArgumentNotValidException"))
                .andExpect(jsonPath("$.errorCode").value("BAD_REQUEST"));
    }

    @Test
    public void shouldResponseBadRequestCodeWhenTryAddNewPatientWithoutOwnerName() throws Exception {
        //given
        Patient patient = new Patient("Felix", "Kon", "Czystej krwi arabskiej", 7, "", "Dudu", "c@a.com");
        String createPatientCommandJson = objectMapper.writeValueAsString(modelMapper.map(patient, CreatePatientCommand.class));
        //when
        mockMvc.perform(post("/patient")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createPatientCommandJson))
                //then
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorMessages").isArray())
                .andExpect(jsonPath("$.errorMessages", hasSize(1)))
                .andExpect(jsonPath("$.errorMessages", hasItem("Property: ownerName; value: ''; message: must not be blank")))
                .andExpect(jsonPath("$.exceptionTypeName").value("MethodArgumentNotValidException"))
                .andExpect(jsonPath("$.errorCode").value("BAD_REQUEST"));
    }

    @Test
    public void shouldResponseBadRequestCodeWhenTryAddNewPatientWithoutOwnerSurname() throws Exception {
        //given
        Patient patient = new Patient("Felix", "Kon", "Czystej krwi arabskiej", 7, "Janina", "", "c@a.com");
        String createPatientCommandJson = objectMapper.writeValueAsString(modelMapper.map(patient, CreatePatientCommand.class));
        //when
        mockMvc.perform(post("/patient")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createPatientCommandJson))
                //then
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorMessages").isArray())
                .andExpect(jsonPath("$.errorMessages", hasSize(1)))
                .andExpect(jsonPath("$.errorMessages", hasItem("Property: ownerSurname; value: ''; message: must not be blank")))
                .andExpect(jsonPath("$.exceptionTypeName").value("MethodArgumentNotValidException"))
                .andExpect(jsonPath("$.errorCode").value("BAD_REQUEST"));
    }


    @Test
    public void shouldResponseBadRequestCodeWhenTryAddNewPatientWithoutEmail() throws Exception {
        //given
        Patient patient = new Patient("Felix", "Kon", "Czystej krwi arabskiej", 7, "Janina", "Dudu", "");
        String createPatientCommandJson = objectMapper.writeValueAsString(modelMapper.map(patient, CreatePatientCommand.class));
        //when
        mockMvc.perform(post("/patient")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createPatientCommandJson))
                //then
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorMessages").isArray())
                .andExpect(jsonPath("$.errorMessages", hasSize(1)))
                .andExpect(jsonPath("$.errorMessages", hasItem("Property: email; value: ''; message: must not be blank")))
                .andExpect(jsonPath("$.exceptionTypeName").value("MethodArgumentNotValidException"))
                .andExpect(jsonPath("$.errorCode").value("BAD_REQUEST"));
    }

    @Test
    public void shouldResponseBadRequestCodeWhenTryAddNewPatientWithInvalidEmailAddress() throws Exception {
        //given
        Patient patient = new Patient("Felix", "Kon", "Czystej krwi arabskiej", 7, "Janina", "Dudu", "ca.com");
        String createPatientCommandJson = objectMapper.writeValueAsString(modelMapper.map(patient, CreatePatientCommand.class));
        //when
        mockMvc.perform(post("/patient")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createPatientCommandJson))
                //then
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorMessages").isArray())
                .andExpect(jsonPath("$.errorMessages", hasSize(1)))
                .andExpect(jsonPath("$.errorMessages", hasItem("Property: email; value: 'ca.com'; message: must be a well-formed email address")))
                .andExpect(jsonPath("$.exceptionTypeName").value("MethodArgumentNotValidException"))
                .andExpect(jsonPath("$.errorCode").value("BAD_REQUEST"));
    }


    @Test
    public void shouldFindTwoPatientForFirstPageOrderAscendingById() throws Exception {
        //given
        List<Patient> patients = Arrays.asList(
                new Patient("Grizzli", "Pies", "Sznaucer", 3, "Marek", "Kowal", "a@a.com"),
                new Patient("Lapka", "Pies", "Maltanczyk", 4, "Marek", "Kowal", "a@a.com")
        );
        patientRepository.saveAll(patients);

        String responseJson = mockMvc.perform(get("/patient")
                        .param("size", "2")
                        .param("page", "0"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        //then
        List<Patient> patientResponse = objectMapper.readValue(responseJson, new TypeReference<List<Patient>>() {});
        assertEquals(2, patientResponse.size());
        for (int i = 0; i < patientResponse.size(); i++) {
            if(i != 0 )
                assertTrue(patientResponse.get(i).getId() > patientResponse.get(i-1).getId());
        }
    }


}