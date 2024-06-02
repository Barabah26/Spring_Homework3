package com.example.spring_homework3.service;

import com.example.spring_homework3.domain.Currency;
import com.example.spring_homework3.domain.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface CustomerService {
    Customer save(Customer obj);
    void delete(Customer obj);
    void deleteAll();
    void saveAll(Customer customer);
    Page<Customer> findAll(Integer from, Integer to, Pageable pageable);
    void deleteById(Long id);
    Optional<Customer> getOne(Long id);
    void createAccountForCustomer(Long id, Currency currency, Double amount);
    void deleteAccountFromCustomer(Long customerId, String accountNumber);
    Customer update(Long id, Customer updatedCustomer);
    void assignAccountsToCustomers();
    void deleteCustomerAccounts(Customer customer);
}
