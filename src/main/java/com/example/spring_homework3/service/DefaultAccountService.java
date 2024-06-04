package com.example.spring_homework3.service;

import com.example.spring_homework3.dao.AccountJpaRepository;
import com.example.spring_homework3.domain.Account;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class DefaultAccountService implements AccountService {
    private final AccountJpaRepository accountJpaRepository;

    @Override
    public void save(Account account) {
        accountJpaRepository.save(account);
    }

    @Override
    public void delete(Account account) {
        accountJpaRepository.delete(account);
    }

    @Override
    public void deleteAll() {
        accountJpaRepository.deleteAll();
    }

    @Override
    public void saveAll(Account account) {
        accountJpaRepository.save(account);
    }

    @Override
    public List<Account> findAll() {
        return accountJpaRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        accountJpaRepository.deleteById(id);
    }

    @Override
    public Optional<Account> getOne(Long id) {
        return accountJpaRepository.findById(id);
    }

    @Override
    public Account findByNumber(UUID number){
        return accountJpaRepository.findByNumber(number);
    }

    @Override
    public Account depositToAccount(UUID accountNumber, double amount) {
        log.info("Depositing {} to account {}", amount, accountNumber);
        if (amount <= 0) {
            log.error("Deposit amount must be greater than zero: {}", amount);
            throw new IllegalArgumentException("Deposit amount must be greater than zero");
        }

        Account account = accountJpaRepository.findByNumber(accountNumber);
        if (account != null) {
            Double newBalance = account.getBalance() + amount;
            log.info("Current balance: {}", account.getBalance());
            account.setBalance(newBalance);
            accountJpaRepository.updateBalanceByNumber(accountNumber, newBalance);
            log.info("New balance: {}", account.getBalance());
            return account;
        } else {
            log.error("Account with number {} not found", accountNumber);
            throw new IllegalArgumentException("Account with number " + accountNumber + " not found");
        }
    }


    @Override
    public boolean withdrawFromAccount(UUID accountNumber, double amount) {
        if (amount <= 0){
            throw new IllegalArgumentException("Incorrect amount" + amount);
        }
        Account account = accountJpaRepository.findByNumber(accountNumber);
        if (account != null) {
            double balance = account.getBalance();
            if (balance >= amount) {
                double newBalance = balance - amount;
                account.setBalance(newBalance);
                accountJpaRepository.updateBalanceByNumber(accountNumber, newBalance);
                return true;
            } else {
                throw new RuntimeException("Insufficient funds in account with number: " + accountNumber);
            }
        } else {
            throw new RuntimeException("Account not found with number: " + accountNumber);
        }
    }


    @Override
    public void transferMoney(UUID fromAccountNumber, UUID toAccountNumber, double amount) {
        if (fromAccountNumber.equals(toAccountNumber)) {
            throw new IllegalArgumentException("From and To account numbers cannot be the same");
        }

        if (amount <= 0){
            throw new IllegalArgumentException("Incorrect amount" + amount);
        }

        Account fromAccount = accountJpaRepository.findByNumber(fromAccountNumber);
        Account toAccount = accountJpaRepository.findByNumber(toAccountNumber);
        if (fromAccount != null && toAccount != null) {
            double balanceFrom = fromAccount.getBalance();
            if (balanceFrom >= amount) {
                double newBalanceFrom = balanceFrom - amount;
                double newBalanceTo = toAccount.getBalance() + amount;

                fromAccount.setBalance(newBalanceFrom);
                toAccount.setBalance(newBalanceTo);

                accountJpaRepository.updateBalanceByNumber(fromAccountNumber, newBalanceFrom);
                accountJpaRepository.updateBalanceByNumber(toAccountNumber, newBalanceTo);
            } else {
                throw new RuntimeException("Insufficient funds in account with number: " + fromAccountNumber);
            }
        } else {
            throw new RuntimeException("One or both accounts not found");
        }
    }

}
