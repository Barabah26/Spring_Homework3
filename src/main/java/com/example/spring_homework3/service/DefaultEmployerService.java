package com.example.spring_homework3.service;

import com.example.spring_homework3.dao.EmployerDao;
import com.example.spring_homework3.domain.Employer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class DefaultEmployerService implements EmployerService {
    private final EmployerDao employerDao;
    @Override
    public Employer save(Employer employer) {
        return employerDao.save(employer);
    }

    @Override
    public boolean delete(Employer employer) {
        return employerDao.delete(employer);
    }

    @Override
    public void deleteAll() {
        employerDao.deleteAll();
    }

    @Override
    public void saveAll(Employer employer) {
        employerDao.saveAll(employer);
    }

    @Override
    public List<Employer> findAll() {
        return employerDao.findAll();
    }

    @Override
    public boolean deleteById(long id) {
        return employerDao.deleteById(id);
    }

    @Override
    public Employer getOne(long id) {
        return employerDao.getOne(id);
    }
}
