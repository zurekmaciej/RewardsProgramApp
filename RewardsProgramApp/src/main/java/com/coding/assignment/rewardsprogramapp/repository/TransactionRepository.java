package com.coding.assignment.rewardsprogramapp.repository;

import com.coding.assignment.rewardsprogramapp.model.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {

    List<Transaction> findByTransactionDateBetween(LocalDate startDate, LocalDate endDate);
}
