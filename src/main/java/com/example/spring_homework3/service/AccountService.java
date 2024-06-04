package com.example.spring_homework3.service;

import com.example.spring_homework3.domain.Account;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AccountService {
    void save(Account account);
    void delete(Account account);
    void deleteAll();
    void saveAll(Account account);
    List<Account> findAll();
    void deleteById(Long id);
    Optional<Account> getOne(Long id);
    Account findByNumber(UUID number);
    Account depositToAccount(UUID accountNumber, double amount);
    boolean withdrawFromAccount(UUID accountNumber, double amount);
    void transferMoney(UUID fromAccountNumber, UUID toAccountNumber, double amount);
}
