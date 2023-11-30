package com.coding.assignment.rewardsprogramapp.controller;

import com.coding.assignment.rewardsprogramapp.model.dto.TransactionDto;
import com.coding.assignment.rewardsprogramapp.service.TransactionService;
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

import java.time.LocalDate;
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
class TransactionControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TransactionService transactionService;

    @Test
    void testAddTransaction() throws Exception {
        UUID transactionId = UUID.randomUUID();
        TransactionDto transactionDto = new TransactionDto(transactionId, LocalDate.now(), 100.0);

        when(transactionService.addTransaction(any(TransactionDto.class))).thenReturn(transactionDto);

        mockMvc.perform(post("/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(transactionDto)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(transactionId.toString()))
                .andExpect(jsonPath("$.transactionDate").exists())
                .andExpect(jsonPath("$.amount").value(100.0));
    }

    @Test
    void testGetTransactionById() throws Exception {
        UUID transactionId = UUID.randomUUID();
        TransactionDto transactionDto = new TransactionDto(transactionId, LocalDate.now(), 100.0);

        when(transactionService.getTransactionById(transactionId)).thenReturn(transactionDto);

        mockMvc.perform(get("/transactions/{id}", transactionId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(transactionId.toString()))
                .andExpect(jsonPath("$.transactionDate").exists())
                .andExpect(jsonPath("$.amount").value(100.0));
    }

    @Test
    void testGetAllTransactions() throws Exception {
        UUID transactionId1 = UUID.randomUUID();
        UUID transactionId2 = UUID.randomUUID();

        TransactionDto transactionDto1 = new TransactionDto(transactionId1, LocalDate.now(), 50.0);
        TransactionDto transactionDto2 = new TransactionDto(transactionId2, LocalDate.now(), 75.0);

        List<TransactionDto> transactionDtoList = List.of(transactionDto1, transactionDto2);

        when(transactionService.getAllTransactions(any(Pageable.class)))
                .thenReturn(new PageImpl<>(transactionDtoList, PageRequest.of(0, 10), 2));

        mockMvc.perform(get("/transactions"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.content[0].id").value(transactionId1.toString()))
                .andExpect(jsonPath("$.content[0].transactionDate").exists())
                .andExpect(jsonPath("$.content[0].amount").value(50.0))
                .andExpect(jsonPath("$.content[1].id").value(transactionId2.toString()))
                .andExpect(jsonPath("$.content[1].transactionDate").exists())
                .andExpect(jsonPath("$.content[1].amount").value(75.0));
    }

    @Test
    void testUpdateTransaction() throws Exception {
        UUID transactionId = UUID.randomUUID();
        TransactionDto updatedTransactionDto = new TransactionDto(transactionId, LocalDate.now(), 150.0);

        when(transactionService.updateTransaction(any(UUID.class), any(TransactionDto.class)))
                .thenReturn(updatedTransactionDto);

        mockMvc.perform(put("/transactions/{id}", transactionId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(updatedTransactionDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(transactionId.toString()))
                .andExpect(jsonPath("$.transactionDate").exists())
                .andExpect(jsonPath("$.amount").value(150.0));
    }

    @Test
    void testDeleteTransaction() throws Exception {
        UUID transactionId = UUID.randomUUID();

        mockMvc.perform(delete("/transactions/{id}", transactionId))
                .andExpect(status().isNoContent());

        verify(transactionService).deleteTransaction(transactionId);
        verifyNoMoreInteractions(transactionService);
    }

    private String asJsonString(final Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
