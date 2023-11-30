package com.coding.assignment.rewardsprogramapp.service;

import com.coding.assignment.rewardsprogramapp.model.dto.TransactionsSummary;
import com.coding.assignment.rewardsprogramapp.model.entity.Transaction;
import com.coding.assignment.rewardsprogramapp.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RewardService {
    private final TransactionRepository transactionRepository;

    public List<TransactionsSummary> calculateRewardPointsForLastThreeMonths() {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusMonths(3).plusDays(1);

        List<Transaction> transactionsForLastThreeMonths = transactionRepository
                .findByTransactionDateBetween(startDate, endDate);

        Map<UUID, List<Transaction>> transactionsByCustomer = transactionsForLastThreeMonths.stream()
                .collect(Collectors.groupingBy(Transaction::getCustomerId));

        return transactionsByCustomer.entrySet().stream()
                .map(entry -> createTransactionsSummary(entry.getKey(), entry.getValue()))
                .toList();
    }

    private TransactionsSummary createTransactionsSummary(UUID customerId, List<Transaction> transactions) {
        Map<String, List<Transaction>> transactionsByMonth = transactions.stream()
                .collect(Collectors.groupingBy(transaction ->
                        transaction.getTransactionDate().getMonth().toString()));

        Map<String, Integer> pointsPerMonth = transactionsByMonth.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey,
                        e -> calculateRewardPoints(e.getValue())));

        int totalPoints = pointsPerMonth.values().stream()
                .mapToInt(Integer::intValue)
                .sum();

        return new TransactionsSummary(customerId, totalPoints, pointsPerMonth);
    }

    private int calculateRewardPoints(List<Transaction> transactions) {
        int totalRewardPoints = 0;

        for (Transaction transaction : transactions) {
            double transactionAmount = transaction.getAmount();

            if (transactionAmount > 100) {
                totalRewardPoints += (int) ((transactionAmount - 100) * 2);
            }

            if (transactionAmount > 50) {
                totalRewardPoints += (int) Math.min(transactionAmount - 50, 50);
            }
        }

        return totalRewardPoints;
    }
}
