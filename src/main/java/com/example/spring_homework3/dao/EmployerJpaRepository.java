package com.example.spring_homework3.dao;

import com.example.spring_homework3.domain.Employer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployerJpaRepository extends JpaRepository<Employer, Integer> {
}
