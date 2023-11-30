package com.coding.assignment.rewardsprogramapp.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDto {
    private UUID id;
    private LocalDate transactionDate;
    private double amount;
}
