package com.example.spring_homework3.service;

import com.example.spring_homework3.dao.EmployerJpaRepository;
import com.example.spring_homework3.domain.Employer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class DefaultEmployerService implements EmployerService {
    private final EmployerJpaRepository employerJpaRepository;
    @Override
    public Employer save(Employer employer) {
        return employerJpaRepository.save(employer);
    }

    @Override
    public void delete(Employer employer) {
        employerJpaRepository.delete(employer);
    }

    @Override
    public void deleteAll() {
        employerJpaRepository.deleteAll();
    }

    @Override
    public void saveAll(Employer employer) {
        employerJpaRepository.save(employer);
    }

    @Override
    public List<Employer> findAll() {
        return employerJpaRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        employerJpaRepository.deleteById(id);
    }

    @Override
    public Optional<Employer> getOne(Long id) {
        return employerJpaRepository.findById(id);
    }
}
