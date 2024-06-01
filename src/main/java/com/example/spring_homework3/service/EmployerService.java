package com.example.spring_homework3.service;

import com.example.spring_homework3.domain.Employer;

import java.util.List;

public interface EmployerService {
    Employer save(Employer employer);

    boolean delete(Employer employer);

    void deleteAll();

    void saveAll(Employer employer);

    List<Employer> findAll();

    boolean deleteById(long id);

    Employer getOne(long id);
}
