package com.coding.assignment.rewardsprogramapp.service;

import com.coding.assignment.rewardsprogramapp.model.dto.TransactionDto;
import com.coding.assignment.rewardsprogramapp.model.entity.Transaction;
import com.coding.assignment.rewardsprogramapp.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    private TransactionService transactionService;

    @BeforeEach
    void setUp() {
        ModelMapper modelMapper = new ModelMapper();
        transactionService = new TransactionService(transactionRepository, modelMapper);
    }

    @Test
    void testGetAllTransactions() {
        List<Transaction> transactionList = new ArrayList<>();
        Transaction transaction = new Transaction();
        transactionList.add(transaction);

        Pageable pageable = mock(Pageable.class);
        Page<Transaction> transactionPage = new PageImpl<>(transactionList);

        when(transactionRepository.findAll(pageable)).thenReturn(transactionPage);

        Page<TransactionDto> result = transactionService.getAllTransactions(pageable);

        assertNotNull(result);
        assertEquals(transactionList.size(), result.getContent().size());
    }

    @Test
    void testAddTransaction() {
        TransactionDto transactionDto = new TransactionDto();
        Transaction transaction = new Transaction();
        when(transactionRepository.saveAndFlush(any(Transaction.class))).thenReturn(transaction);

        TransactionDto result = transactionService.addTransaction(transactionDto);

        assertNotNull(result);
        assertEquals(transaction.getId(), result.getId());
    }

    @Test
    void testGetTransactionById() {
        UUID id = UUID.randomUUID();
        Transaction transaction = new Transaction();
        when(transactionRepository.findById(id)).thenReturn(Optional.of(transaction));

        TransactionDto result = transactionService.getTransactionById(id);

        assertNotNull(result);
        assertEquals(transaction.getId(), result.getId());
    }

    @Test
    void testUpdateTransaction() {
        UUID id = UUID.randomUUID();
        TransactionDto transactionDto = new TransactionDto(id, LocalDate.now(), 10);
        Transaction existingTransaction = new Transaction();
        when(transactionRepository.findById(id)).thenReturn(Optional.of(existingTransaction));
        when(transactionRepository.saveAndFlush(any(Transaction.class))).thenReturn(existingTransaction);

        TransactionDto result = transactionService.updateTransaction(id, transactionDto);

        assertNotNull(result);
        assertEquals(existingTransaction.getId(), result.getId());
    }

    @Test
    void testDeleteTransaction() {
        UUID id = UUID.randomUUID();

        transactionService.deleteTransaction(id);

        verify(transactionRepository, times(1)).deleteById(id);
    }
}