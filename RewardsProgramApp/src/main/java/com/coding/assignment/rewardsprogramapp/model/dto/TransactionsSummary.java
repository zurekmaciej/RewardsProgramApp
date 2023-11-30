package com.coding.assignment.rewardsprogramapp.model.dto;

import java.util.Map;
import java.util.UUID;

public record TransactionsSummary(UUID customerId, int totalPoints, Map<String, Integer> pointsPerMonth) {
}
