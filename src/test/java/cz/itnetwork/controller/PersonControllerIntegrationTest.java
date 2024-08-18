package cz.itnetwork.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.itnetwork.dto.InvoiceDTO;
import cz.itnetwork.dto.PaginatedResponse;
import cz.itnetwork.dto.PersonDTO;
import cz.itnetwork.dto.PersonStatisticsDTO;
import cz.itnetwork.service.PersonService;
import cz.itnetwork.utils.TestDataFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class PersonControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PersonService personService;

    private static Map<String, Object> getStringObjectMap() {
        PersonStatisticsDTO stat1 = new PersonStatisticsDTO(1L, "Person 1", 1000L);
        PersonStatisticsDTO stat2 = new PersonStatisticsDTO(2L, "Person 2", 2000L);
        PersonStatisticsDTO stat3 = new PersonStatisticsDTO(3L, "Person 3", 3000L);

        PaginatedResponse<PersonStatisticsDTO> paginatedResponse = new PaginatedResponse<>(
                Arrays.asList(stat1, stat2),
                1, 1, 2
        );

        List<PersonStatisticsDTO> top5ByRevenue = Arrays.asList(stat3, stat2, stat1);

        Map<String, Object> mockResponse = new HashMap<>();
        mockResponse.put("paginatedData", paginatedResponse);
        mockResponse.put("top5ByRevenue", top5ByRevenue);
        return mockResponse;
    }

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
                .andExpect(jsonPath("$._id").value(1L))
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

    @Test
    void getPersons_ReturnsPagedResult() throws Exception {
        PaginatedResponse<PersonDTO> mockResponse = new PaginatedResponse<>(
                Collections.singletonList(TestDataFactory.createValidPersonDTO()),
                1, 1, 1
        );

        when(personService.getPersons(any())).thenReturn(mockResponse);

        mockMvc.perform(get("/api/persons")
                        .param("page", "1")
                        .param("limit", "10")
                        .param("sort", "name,asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currentPage").value(1))
                .andExpect(jsonPath("$.totalPages").value(1))
                .andExpect(jsonPath("$.totalItems").value(1))
                .andExpect(jsonPath("$.items").isArray())
                .andExpect(jsonPath("$.items[0].name").exists());
    }

    @Test
    void getPersonSales_ReturnsPagedResult() throws Exception {
        String identificationNumber = "12345678";
        PaginatedResponse<InvoiceDTO> mockResponse = new PaginatedResponse<>(
                Collections.singletonList(new InvoiceDTO()),
                1, 1, 1
        );

        when(personService.getPersonSales(eq(identificationNumber), eq(1), eq(10))).thenReturn(mockResponse);

        mockMvc.perform(get("/api/persons/identification/{identificationNumber}/sales", identificationNumber)
                        .param("page", "1")
                        .param("limit", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currentPage").value(1))
                .andExpect(jsonPath("$.totalPages").value(1))
                .andExpect(jsonPath("$.totalItems").value(1))
                .andExpect(jsonPath("$.items").isArray())
                .andExpect(jsonPath("$.items[0]").exists());
    }

    @Test
    void getPersonPurchases_ReturnsPagedResult() throws Exception {
        String identificationNumber = "12345678";
        PaginatedResponse<InvoiceDTO> mockResponse = new PaginatedResponse<>(
                Collections.singletonList(new InvoiceDTO()),
                1, 1, 1
        );

        when(personService.getPersonPurchases(eq(identificationNumber), eq(1), eq(10))).thenReturn(mockResponse);

        mockMvc.perform(get("/api/persons/identification/{identificationNumber}/purchases", identificationNumber)
                        .param("page", "1")
                        .param("limit", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currentPage").value(1))
                .andExpect(jsonPath("$.totalPages").value(1))
                .andExpect(jsonPath("$.totalItems").value(1))
                .andExpect(jsonPath("$.items").isArray())
                .andExpect(jsonPath("$.items[0]").exists());
    }

    @Test
    void getPersonStatistics_ReturnsCorrectMap() throws Exception {
        Map<String, Object> mockResponse = getStringObjectMap();

        when(personService.getPersonStatistics(eq(1), eq(10), eq("name,asc"))).thenReturn(mockResponse);

        mockMvc.perform(get("/api/persons/statistics")
                        .param("page", "1")
                        .param("limit", "10")
                        .param("sort", "name,asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.paginatedData.currentPage").value(1))
                .andExpect(jsonPath("$.paginatedData.totalPages").value(1))
                .andExpect(jsonPath("$.paginatedData.totalItems").value(2))
                .andExpect(jsonPath("$.paginatedData.items").isArray())
                .andExpect(jsonPath("$.paginatedData.items[0].personId").value(1))
                .andExpect(jsonPath("$.paginatedData.items[0].personName").value("Person 1"))
                .andExpect(jsonPath("$.paginatedData.items[0].revenue").value(1000))
                .andExpect(jsonPath("$.paginatedData.items[1].personId").value(2))
                .andExpect(jsonPath("$.paginatedData.items[1].personName").value("Person 2"))
                .andExpect(jsonPath("$.paginatedData.items[1].revenue").value(2000))
                .andExpect(jsonPath("$.top5ByRevenue").isArray())
                .andExpect(jsonPath("$.top5ByRevenue[0].personId").value(3))
                .andExpect(jsonPath("$.top5ByRevenue[0].personName").value("Person 3"))
                .andExpect(jsonPath("$.top5ByRevenue[0].revenue").value(3000))
                .andExpect(jsonPath("$.top5ByRevenue[1].personId").value(2))
                .andExpect(jsonPath("$.top5ByRevenue[2].personId").value(1));
    }
}