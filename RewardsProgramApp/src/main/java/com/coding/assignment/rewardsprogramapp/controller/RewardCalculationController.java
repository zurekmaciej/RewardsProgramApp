package com.coding.assignment.rewardsprogramapp.controller;

import com.coding.assignment.rewardsprogramapp.model.dto.TransactionsSummary;
import com.coding.assignment.rewardsprogramapp.service.RewardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/rewards")
@RequiredArgsConstructor
public class RewardCalculationController {
    private final RewardService rewardService;

    @Operation(summary = "Gets calculated reward points for last three months for all customers")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Page.class))})})
    @GetMapping("/calculate")
    public ResponseEntity<List<TransactionsSummary>> calculateRewardsForCustomerLastThreeMonths() {
        log.info("Calculating rewards for the last three months");
        List<TransactionsSummary> response = rewardService.calculateRewardPointsForLastThreeMonths();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
