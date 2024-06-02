package com.example.spring_homework3.service;

import com.example.spring_homework3.dao.AccountJpaRepository;
import com.example.spring_homework3.dao.CustomerJpaRepository;
import com.example.spring_homework3.domain.Account;
import com.example.spring_homework3.domain.Currency;
import com.example.spring_homework3.domain.Customer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class DefaultCustomerService implements CustomerService {
    private final CustomerJpaRepository customerJpaRepository;
    private final AccountJpaRepository accountJpaRepository;

    @Override
    public Customer save(Customer customer) {
        return customerJpaRepository.save(customer);
    }

    @Override
    public void delete(Customer customer) {
        customerJpaRepository.delete(customer);
    }

    @Override
    public void deleteAll() {
        customerJpaRepository.deleteAll();
    }

    @Override
    public void saveAll(Customer customer) {
        customerJpaRepository.save(customer);
    }

    @Override
    public Page<Customer> findAll(Integer from, Integer to, Pageable pageable) {
        return customerJpaRepository.findAllInRange(from, to, pageable);
    }

    @Override
    public void deleteById(Long id) {
        customerJpaRepository.deleteById(id);
    }

    @Override
    public Optional<Customer> getOne(Long id) {
        return customerJpaRepository.findById(id);
    }


    @Override
    public void createAccountForCustomer(Long id, Currency currency, Double amount) {
        Customer customer = customerJpaRepository.getOne(id);

        if (customer == null) {
            throw new IllegalArgumentException("Customer not found with id: " + id);
        }

        Account account = new Account(currency, customer);
        account.setBalance(amount);

        boolean accountExists = customer.getAccounts().stream()
                .anyMatch(e -> e.getNumber().equals(account.getNumber()));

        if (accountExists) {
            throw new IllegalArgumentException("Account with number " + account.getNumber() + " already exists for customer with id: " + id);
        }

        List<Account> customerAccounts = customer.getAccounts();
        customerAccounts.add(account);

        accountJpaRepository.save(account);
    }

    @Override
    public void deleteCustomerAccounts(Customer customer) {
        List<Account> accounts = customer.getAccounts();
        if (!accounts.isEmpty()){
            for (Account account : accounts) {
                accountJpaRepository.deleteById(account.getId());
            }
            accounts.clear();
//            accountJpaRepository.updateBalanceByNumber(customer, );
        } else {
            throw new IllegalArgumentException("Accounts are absent");
        }

    }

    @Override
    public void deleteAccountFromCustomer(Long customerId, String accountNumber) {
        Customer customer = customerJpaRepository.getOne(customerId);
        if (customer != null) {
            Account accountToDelete = customer.getAccounts().stream()
                    .filter(account -> account.getNumber().equals(accountNumber))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Account with number " + accountNumber + " not found"));

            // Видаляємо акаунт з акаунтів клієнта
            boolean removed = customer.getAccounts().removeIf(account -> account.getNumber().equals(accountNumber));
            if (removed) {
                accountJpaRepository.delete(accountToDelete);
            }
        } else {
            throw new IllegalArgumentException("Customer not found with id: " + customerId);
        }
    }

    @Override
    public Customer update(Long id, Customer updatedCustomer) {
        Customer currentCustomer = customerJpaRepository.getOne(id);
        if (currentCustomer == null) {
            throw new IllegalArgumentException("Customer with id " + id + " not found");
        }

        currentCustomer.setName(updatedCustomer.getName());
        currentCustomer.setEmail(updatedCustomer.getEmail());
        currentCustomer.setAge(updatedCustomer.getAge());

        for (Account account : currentCustomer.getAccounts()) {
            account.setCustomer(currentCustomer);
            accountJpaRepository.save(account);
        }

        customerJpaRepository.updateCustomerById(id, updatedCustomer.getName(), updatedCustomer.getEmail(), updatedCustomer.getAge());
        return updatedCustomer;
    }

    @Override
    public void assignAccountsToCustomers() {

        List<Account> allAccounts = accountJpaRepository.findAll();
        List<Customer> allCustomers = customerJpaRepository.findAll();

        for (Account account : allAccounts) {
            Customer customer = account.getCustomer();
            if (customer != null) {
                for (Customer cust : allCustomers) {
                    if (cust.getId().equals(customer.getId())) {
                        boolean accountExists = cust.getAccounts().stream()
                                .anyMatch(existingAccount -> existingAccount.getNumber().equals(account.getNumber()));

                        if (!accountExists) {
                            cust.getAccounts().add(account);
                        } else {
                            log.warn("Account with number " + account.getNumber() + " already exists for customer with id: " + customer.getId());
                        }
                        break;
                    }
                }
            }
        }
    }
}
