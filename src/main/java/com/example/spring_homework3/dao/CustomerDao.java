package com.example.spring_homework3.dao;

import com.example.spring_homework3.domain.Customer;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CustomerDao implements Dao<Customer> {

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    @Transactional
    public Customer save(Customer customer) {
        if (customer.getId() == null) {
            entityManager.persist(customer);
        } else {
            entityManager.merge(customer);
        }
        return customer;
    }

    @Override
    @Transactional
    public boolean delete(Customer customer) {
        if (entityManager.contains(customer)) {
            entityManager.remove(customer);
            return true;
        } else {
            Customer managedCustomer = entityManager.find(Customer.class, customer.getId());
            if (managedCustomer != null) {
                entityManager.remove(managedCustomer);
                return true;
            }
        }
        return false;
    }

    @Override
    @Transactional
    public void deleteAll() {
        entityManager.createQuery("DELETE FROM Customer").executeUpdate();
    }

    @Override
    @Transactional
    public void saveAll(Customer customer) {
        save(customer);
    }

    @Override
    public List<Customer> findAll() {
        return entityManager.createQuery("SELECT c FROM Customer c", Customer.class).getResultList();
    }

    @Override
    @Transactional
    public boolean deleteById(long id) {
        Customer customer = entityManager.find(Customer.class, id);
        if (customer != null) {
            entityManager.remove(customer);
            return true;
        }
        return false;
    }

    @Override
    public Customer getOne(long id) {
        return entityManager.find(Customer.class, id);
    }

    @Transactional
    public Customer update(Customer updatedCustomer) {
        Customer existingCustomer = entityManager.find(Customer.class, updatedCustomer.getId());
        if (existingCustomer == null) {
            throw new RuntimeException("Customer not found with id: " + updatedCustomer.getId());
        }
        existingCustomer.setName(updatedCustomer.getName());
        existingCustomer.setEmail(updatedCustomer.getEmail());
        existingCustomer.setAge(updatedCustomer.getAge());
        entityManager.merge(existingCustomer);
        return existingCustomer;
    }
}
