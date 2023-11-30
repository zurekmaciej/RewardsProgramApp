package com.coding.assignment.rewardsprogramapp.repository;

import com.coding.assignment.rewardsprogramapp.model.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CustomerRepository extends JpaRepository<Customer, UUID> {
}
