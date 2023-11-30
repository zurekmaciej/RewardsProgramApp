package com.coding.assignment.rewardsprogramapp.service;

import com.coding.assignment.rewardsprogramapp.model.dto.CustomerDto;
import com.coding.assignment.rewardsprogramapp.model.entity.Customer;
import com.coding.assignment.rewardsprogramapp.repository.CustomerRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final ModelMapper modelMapper;

    public Page<CustomerDto> getAllCustomers(Pageable pageable) {
        Page<Customer> customers = customerRepository.findAll(pageable);
        return customers.map(this::convertToDto);
    }

    public CustomerDto addCustomer(CustomerDto customerDto) {
        Customer customer = convertToEntity(customerDto);
        Customer savedCustomer = customerRepository.saveAndFlush(customer);
        return convertToDto(savedCustomer);
    }

    public CustomerDto getCustomerById(UUID id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found with id: " + id));
        return convertToDto(customer);
    }

    public CustomerDto updateCustomer(UUID id, CustomerDto customerDto) {
        Customer existingCustomer = customerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found with id: " + id));

        Customer updatedCustomer = updateCustomerFromDto(existingCustomer, customerDto);
        Customer savedCustomer = customerRepository.saveAndFlush(updatedCustomer);
        return convertToDto(savedCustomer);
    }

    public void deleteCustomer(UUID id) {
        customerRepository.deleteById(id);
    }

    private CustomerDto convertToDto(Customer customer) {
        return modelMapper.map(customer, CustomerDto.class);
    }

    private Customer convertToEntity(CustomerDto customerDto) {
        return modelMapper.map(customerDto, Customer.class);
    }

    private Customer updateCustomerFromDto(Customer existingCustomer, CustomerDto customerDto) {
        existingCustomer.setEmail(customerDto.getEmail());
        existingCustomer.setName(customerDto.getName());
        return existingCustomer;
    }
}
