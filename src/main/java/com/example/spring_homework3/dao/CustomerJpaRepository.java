package com.example.spring_homework3.dao;

import com.example.spring_homework3.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerJpaRepository extends JpaRepository<Customer, Integer> {
}
