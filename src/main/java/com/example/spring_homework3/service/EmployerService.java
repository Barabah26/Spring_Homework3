package com.example.spring_homework3.service;

import com.example.spring_homework3.domain.Employer;

import java.util.List;
import java.util.Optional;

public interface EmployerService {
    Employer save(Employer employer);

    void delete(Employer employer);

    void deleteAll();

    void saveAll(Employer employer);

    List<Employer> findAll();

    void deleteById(Long id);

    Optional<Employer> getOne(Long id);
}
