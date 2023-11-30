package com.coding.assignment.rewardsprogramapp.service;

import com.coding.assignment.rewardsprogramapp.model.dto.TransactionsSummary;
import com.coding.assignment.rewardsprogramapp.model.entity.Customer;
import com.coding.assignment.rewardsprogramapp.model.entity.Transaction;
import com.coding.assignment.rewardsprogramapp.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RewardServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private RewardService rewardService;


    @Test
    void testCalculateRewardPointsForLastThreeMonths() {
        Customer customer1 = new Customer()
                .setId(UUID.randomUUID())
                .setEmail("email1@test.com")
                .setName("testName1");

        Customer customer2 = new Customer()
                .setId(UUID.randomUUID())
                .setEmail("email2@test.com")
                .setName("testName2");

        Transaction transaction1 = new Transaction(UUID.randomUUID(), LocalDate.now(), 120.0, customer1);
        Transaction transaction2 = new Transaction(UUID.randomUUID(), LocalDate.now().minusMonths(1), 80.0, customer1);
        Transaction transaction3 = new Transaction(UUID.randomUUID(), LocalDate.now(), 160.0, customer2);
        Transaction transaction4 = new Transaction(UUID.randomUUID(), LocalDate.now().minusMonths(2), 60.0, customer2);
        List<Transaction> transactions = List.of(transaction1, transaction2, transaction3, transaction4);

        Map<String, Integer> expectedCustomer1SummaryMap = Map.of(getMonthName(LocalDate.now()), 90, getMonthName(LocalDate.now().minusMonths(1)), 30);
        TransactionsSummary expectedCustomer1Summary = new TransactionsSummary(customer1.getId(), 120, expectedCustomer1SummaryMap);

        Map<String, Integer> expectedCustomer2SummaryMap = Map.of(getMonthName(LocalDate.now()), 170, getMonthName(LocalDate.now().minusMonths(2)), 10);
        TransactionsSummary expectedCustomer2Summary = new TransactionsSummary(customer2.getId(), 180, expectedCustomer2SummaryMap);

        List<TransactionsSummary> expected = List.of(expectedCustomer1Summary, expectedCustomer2Summary);


        when(transactionRepository.findByTransactionDateBetween(LocalDate.now().minusMonths(3).plusDays(1),
                LocalDate.now())).thenReturn(transactions);

        List<TransactionsSummary> actual = rewardService.calculateRewardPointsForLastThreeMonths();

        assertThat(actual, hasSize(2));
        assertThat(actual, containsInAnyOrder(expected.toArray()));
    }

    private String getMonthName(LocalDate localDate) {
        Month currentMonth = localDate.getMonth();
        return currentMonth.getDisplayName(TextStyle.FULL, Locale.US).toUpperCase();
    }
}