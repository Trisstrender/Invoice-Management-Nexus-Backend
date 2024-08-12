package cz.itnetwork.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.itnetwork.dto.PersonDTO;
import cz.itnetwork.service.PersonService;
import cz.itnetwork.utils.TestDataFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class PersonControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PersonService personService;

    @Test
    void addPerson_ValidPerson_ReturnsCreatedPerson() throws Exception {
        PersonDTO inputDTO = TestDataFactory.createValidPersonDTO();
        PersonDTO outputDTO = TestDataFactory.createValidPersonDTO();
        outputDTO.setId(1L);

        when(personService.addPerson(any(PersonDTO.class))).thenReturn(outputDTO);

        mockMvc.perform(post("/api/persons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$._id").value(1L))  // Changed from $.id to $._id
                .andExpect(jsonPath("$.name").value(inputDTO.getName()));
    }

    @Test
    void addPerson_InvalidPerson_ReturnsBadRequest() throws Exception {
        PersonDTO invalidDTO = TestDataFactory.createValidPersonDTO();
        invalidDTO.setName("");  // Invalid: empty name

        mockMvc.perform(post("/api/persons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getPerson_ExistingId_ReturnsPerson() throws Exception {
        Long personId = 1L;
        PersonDTO personDTO = TestDataFactory.createValidPersonDTO();
        personDTO.setId(personId);

        when(personService.getPersonById(personId)).thenReturn(personDTO);

        mockMvc.perform(get("/api/persons/{id}", personId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._id").value(personId))
                .andExpect(jsonPath("$.name").value(personDTO.getName()));
    }

    @Test
    void updatePerson_ValidPerson_ReturnsUpdatedPerson() throws Exception {
        Long personId = 1L;
        PersonDTO inputDTO = TestDataFactory.createValidPersonDTO();
        PersonDTO updatedDTO = TestDataFactory.createValidPersonDTO();
        updatedDTO.setId(personId);

        when(personService.updatePerson(eq(personId), any(PersonDTO.class))).thenReturn(updatedDTO);

        mockMvc.perform(put("/api/persons/{id}", personId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._id").value(personId))
                .andExpect(jsonPath("$.name").value(updatedDTO.getName()));
    }

    @Test
    void deletePerson_ExistingId_ReturnsNoContent() throws Exception {
        long personId = 1L;

        doNothing().when(personService).removePerson(personId);

        mockMvc.perform(delete("/api/persons/{id}", personId))
                .andExpect(status().isNoContent());
    }
}