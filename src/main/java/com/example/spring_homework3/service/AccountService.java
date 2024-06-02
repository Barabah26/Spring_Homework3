package com.example.spring_homework3.service;

import com.example.spring_homework3.domain.Account;

import java.util.List;
import java.util.Optional;

public interface AccountService {
    void save(Account account);
    void delete(Account account);
    void deleteAll();
    void saveAll(Account account);
    List<Account> findAll();
    void deleteById(Long id);
    Optional<Account> getOne(Long id);
    Account findByNumber(String number);
    Account depositToAccount(String accountNumber, double amount);
    boolean withdrawFromAccount(String accountNumber, double amount);
    void transferMoney(String fromAccountNumber, String toAccountNumber, double amount);
}
