package com.coding.assignment.rewardsprogramapp.model.dto;

import java.util.UUID;

public record RewardCalculationRequest(UUID customerId, int year, int month) {
}
