package com.coding.assignment.rewardsprogramapp.controller;

import com.coding.assignment.rewardsprogramapp.model.dto.TransactionsSummary;
import com.coding.assignment.rewardsprogramapp.service.RewardService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.MockMvcPrint;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RewardCalculationController.class)
@AutoConfigureMockMvc(print = MockMvcPrint.SYSTEM_ERR)
class RewardCalculationControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RewardService rewardService;

    @Test
    void testCalculateRewardsForCustomerLastThreeMonths() throws Exception {
        TransactionsSummary summary = new TransactionsSummary(UUID.randomUUID(), 100, Map.of("November", 50));
        List<TransactionsSummary> summaries = List.of(summary);

        when(rewardService.calculateRewardPointsForLastThreeMonths()).thenReturn(summaries);

        mockMvc.perform(get("/rewards/calculate")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].customerId").exists())
                .andExpect(jsonPath("$[0].totalPoints").value(100))
                .andExpect(jsonPath("$[0].pointsPerMonth.November").value(50));
    }
}
