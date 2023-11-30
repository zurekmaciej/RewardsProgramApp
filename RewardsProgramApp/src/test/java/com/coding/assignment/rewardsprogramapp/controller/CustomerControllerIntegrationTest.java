package com.coding.assignment.rewardsprogramapp.controller;

import com.coding.assignment.rewardsprogramapp.model.dto.CustomerDto;
import com.coding.assignment.rewardsprogramapp.service.CustomerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CustomerControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CustomerService customerService;

    @Test
    void testAddCustomer() throws Exception {
        UUID customerId = UUID.randomUUID();
        CustomerDto customerDto = new CustomerDto(customerId, "Test Customer", "email@test.com");

        when(customerService.addCustomer(any(CustomerDto.class))).thenReturn(customerDto);

        mockMvc.perform(post("/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(customerDto)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(customerId.toString()))
                .andExpect(jsonPath("$.name").value("Test Customer"))
                .andExpect(jsonPath("$.email").value("email@test.com"));
    }

    @Test
    void testGetCustomerById() throws Exception {
        UUID customerId = UUID.randomUUID();
        CustomerDto customerDto = new CustomerDto(customerId, "Test Customer", "email@test.com");

        when(customerService.getCustomerById(customerId)).thenReturn(customerDto);

        mockMvc.perform(get("/customers/{id}", customerId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(customerId.toString()))
                .andExpect(jsonPath("$.name").value("Test Customer"))
                .andExpect(jsonPath("$.email").value("email@test.com"));
    }

    @Test
    void testGetAllCustomers() throws Exception {
        UUID customerId1 = UUID.randomUUID();
        UUID customerId2 = UUID.randomUUID();

        CustomerDto customerDto1 = new CustomerDto(customerId1, "Test Customer 1", "email1@test.com");
        CustomerDto customerDto2 = new CustomerDto(customerId2, "Test Customer 2", "email2@test.com");

        List<CustomerDto> customerDtoList = List.of(customerDto1, customerDto2);

        when(customerService.getAllCustomers(any(Pageable.class)))
                .thenReturn(new PageImpl<>(customerDtoList, PageRequest.of(0, 10), 2));

        mockMvc.perform(get("/customers"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.content[0].id").value(customerId1.toString()))
                .andExpect(jsonPath("$.content[0].name").value("Test Customer 1"))
                .andExpect(jsonPath("$.content[0].email").value("email1@test.com"))
                .andExpect(jsonPath("$.content[1].id").value(customerId2.toString()))
                .andExpect(jsonPath("$.content[1].name").value("Test Customer 2"))
                .andExpect(jsonPath("$.content[1].email").value("email2@test.com"));
    }

    @Test
    void testUpdateCustomer() throws Exception {
        UUID customerId = UUID.randomUUID();
        CustomerDto updatedCustomerDto = new CustomerDto(customerId, "Test Customer", "email2@test.com");

        when(customerService.updateCustomer(any(UUID.class), any(CustomerDto.class)))
                .thenReturn(updatedCustomerDto);

        mockMvc.perform(put("/customers/{id}", customerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(updatedCustomerDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(customerId.toString()))
                .andExpect(jsonPath("$.name").value("Test Customer"))
                .andExpect(jsonPath("$.email").value("email2@test.com"));
    }

    @Test
    void testDeleteCustomer() throws Exception {
        UUID customerId = UUID.randomUUID();

        mockMvc.perform(delete("/customers/{id}", customerId))
                .andExpect(status().isNoContent());

        verify(customerService).deleteCustomer(customerId);
        verifyNoMoreInteractions(customerService);
    }

    private String asJsonString(final Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
