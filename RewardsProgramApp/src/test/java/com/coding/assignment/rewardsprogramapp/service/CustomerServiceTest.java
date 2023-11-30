package com.coding.assignment.rewardsprogramapp.service;

import com.coding.assignment.rewardsprogramapp.model.dto.CustomerDto;
import com.coding.assignment.rewardsprogramapp.model.entity.Customer;
import com.coding.assignment.rewardsprogramapp.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

// slightly different approach than in TransactionServiceTest, to show different possibilities

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    private CustomerService customerService;

    @BeforeEach
    void setUp() {
        ModelMapper modelMapper = new ModelMapper();
        customerService = new CustomerService(customerRepository, modelMapper);
    }

    @Test
    void testGetAllCustomers() {
        List<Customer> customerList = new ArrayList<>();
        Customer customer = new Customer();
        customerList.add(customer);

        Pageable pageable = mock(Pageable.class);
        Page<Customer> customerPage = new PageImpl<>(customerList);

        when(customerRepository.findAll(pageable)).thenReturn(customerPage);

        Page<CustomerDto> result = customerService.getAllCustomers(pageable);

        assertNotNull(result);
        verify(customerRepository).findAll(pageable);
        verifyNoMoreInteractions(customerRepository);
    }

    @Test
    void testAddCustomer() {
        UUID customerId = UUID.randomUUID();
        CustomerDto customerDto = new CustomerDto();
        customerDto.setName("New Customer");

        Customer customer = new Customer();
        customer.setId(customerId);
        customer.setName("Test Customer");

        when(customerRepository.saveAndFlush(any(Customer.class))).thenReturn(customer);

        CustomerDto result = customerService.addCustomer(customerDto);

        assertNotNull(result);
        assertEquals(customerId, result.getId());
        assertEquals("Test Customer", result.getName());
        verify(customerRepository).saveAndFlush(any(Customer.class));
        verifyNoMoreInteractions(customerRepository);
    }

    @Test
    void testGetCustomerById() {
        UUID customerId = UUID.randomUUID();
        Customer customer = new Customer();
        customer.setId(customerId);
        customer.setName("Test Customer");

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));

        CustomerDto result = customerService.getCustomerById(customerId);

        assertNotNull(result);
        assertEquals(customerId, result.getId());
        assertEquals("Test Customer", result.getName());
        verify(customerRepository).findById(customerId);
        verifyNoMoreInteractions(customerRepository);
    }

    @Test
    void testUpdateCustomer() {
        UUID customerId = UUID.randomUUID();
        CustomerDto customerDto = new CustomerDto();
        customerDto.setName("Updated Customer");

        Customer customer = new Customer();
        customer.setId(customerId);
        customer.setName("Test Customer");

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        when(customerRepository.saveAndFlush(any(Customer.class))).thenReturn(customer);

        CustomerDto result = customerService.updateCustomer(customerId, customerDto);

        assertNotNull(result);
        assertEquals(customerId, result.getId());
        assertEquals("Updated Customer", result.getName());
        verify(customerRepository).findById(customerId);
        verify(customerRepository).saveAndFlush(any(Customer.class));
        verifyNoMoreInteractions(customerRepository);
    }

    @Test
    void testDeleteCustomer() {
        UUID customerId = UUID.randomUUID();
        customerService.deleteCustomer(customerId);
        verify(customerRepository).deleteById(customerId);
        verifyNoMoreInteractions(customerRepository);
    }
}