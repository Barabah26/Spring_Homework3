package com.example.spring_homework3.dao;

import com.example.spring_homework3.domain.Employer;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class EmployerDao implements Dao<Employer> {
    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    @Transactional
    public Employer save(Employer employer) {
        if (employer.getId() == null) {
            entityManager.persist(employer);
        } else {
            entityManager.merge(employer);
        }
        return employer;
    }

    @Override
    @Transactional
    public boolean delete(Employer employer) {
        if (entityManager.contains(employer)) {
            entityManager.remove(employer);
            return true;
        } else {
            Employer managedEmployer = entityManager.find(Employer.class, employer.getId());
            if (managedEmployer != null) {
                entityManager.remove(managedEmployer);
                return true;
            }
        }
        return false;
    }

    @Override
    @Transactional
    public void deleteAll() {
        entityManager.createQuery("DELETE FROM Employer").executeUpdate();
    }

    @Override
    @Transactional
    public void saveAll(Employer employer) {
        save(employer);
    }

    @Override
    @Transactional
    public List<Employer> findAll() {
        return entityManager.createQuery("SELECT e FROM Employer e", Employer.class).getResultList();

    }

    @Override
    @Transactional
    public boolean deleteById(long id) {
        Employer employer = entityManager.find(Employer.class, id);
        if (employer != null) {
            entityManager.remove(employer);
            return true;
        }
        return false;
    }

    @Override
    @Transactional
    public Employer getOne(long id) {
        return entityManager.find(Employer.class, id);
    }

    @Transactional
    public Employer update(Employer employer) {
        Employer existingEmployer = entityManager.find(Employer.class, employer.getId());
        if (existingEmployer == null) {
            throw new RuntimeException("Customer not found with id: " + employer.getId());
        }
        existingEmployer.setName(employer.getName());
        existingEmployer.setAddress(employer.getAddress());
        entityManager.merge(existingEmployer);
        return existingEmployer;
    }
}
