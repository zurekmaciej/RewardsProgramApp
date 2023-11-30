package com.coding.assignment.rewardsprogramapp.service;

import com.coding.assignment.rewardsprogramapp.model.dto.TransactionDto;
import com.coding.assignment.rewardsprogramapp.model.entity.Transaction;
import com.coding.assignment.rewardsprogramapp.repository.TransactionRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final ModelMapper modelMapper;

    public Page<TransactionDto> getAllTransactions(Pageable pageable) {
        Page<Transaction> transactions = transactionRepository.findAll(pageable);
        return transactions.map(this::convertToDto);
    }

    public TransactionDto addTransaction(TransactionDto transactionDto) {
        Transaction transaction = convertToEntity(transactionDto);
        Transaction savedTransaction = transactionRepository.saveAndFlush(transaction);
        return convertToDto(savedTransaction);
    }

    public TransactionDto getTransactionById(UUID id) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Transaction not found with id: " + id));
        return convertToDto(transaction);
    }

    public TransactionDto updateTransaction(UUID id, TransactionDto transactionDto) {
        Transaction existingTransaction = transactionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Transaction not found with id: " + id));

        Transaction updatedTransaction = updateTransactionFromDto(existingTransaction, transactionDto);
        Transaction savedTransaction = transactionRepository.saveAndFlush(updatedTransaction);
        return convertToDto(savedTransaction);
    }

    public void deleteTransaction(UUID id) {
        transactionRepository.deleteById(id);
    }

    private TransactionDto convertToDto(Transaction transaction) {
        return modelMapper.map(transaction, TransactionDto.class);
    }

    private Transaction convertToEntity(TransactionDto transactionDto) {
        return modelMapper.map(transactionDto, Transaction.class);
    }

    private Transaction updateTransactionFromDto(Transaction existingTransaction, TransactionDto transactionDto) {
        existingTransaction.setTransactionDate(transactionDto.getTransactionDate());
        existingTransaction.setAmount(transactionDto.getAmount());
        return existingTransaction;
    }
}
