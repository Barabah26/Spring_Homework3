package com.example.spring_homework3.controller;


import com.example.spring_homework3.domain.Account;
import com.example.spring_homework3.service.DefaultAccountService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import io.swagger.models.auth.In;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/accounts")
@CrossOrigin(origins = "http://localhost:3000")
@Slf4j
public class AccountController {
    private final DefaultAccountService accountService;
    private final ObjectMapper objectMapper;

    @Autowired
    public AccountController(DefaultAccountService accountService, ObjectMapper objectMapper) {
        this.accountService = accountService;
        this.objectMapper = objectMapper;
    }

    @ModelAttribute
    public void configureObjectMapper() {
        SimpleFilterProvider filters = new SimpleFilterProvider();
        filters.addFilter("accountFilter", SimpleBeanPropertyFilter.serializeAll());
        filters.addFilter("customerFilter", SimpleBeanPropertyFilter.serializeAllExcept("accounts"));
        objectMapper.setFilterProvider(filters);
    }

    @Operation(summary = "Get all accounts")
    @GetMapping("/all")
    public List<Account> getAllAccounts() {
        return accountService.findAll();
    }

    @DeleteMapping("/{id}")
    public void deleteAccountById(@PathVariable Long id) {
        accountService.deleteById(id);
    }

    @Operation(summary = "Get account by id")
    @GetMapping("/{id}")
    public ResponseEntity<?> getAccountById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(accountService.getOne(id));
        } catch (RuntimeException e) {
            log.error("Account with id " + id + " not found", e);
            return ResponseEntity.badRequest().body("Account with id " + id + " not found");
        }
    }

    @Operation(summary = "Deposit an amount to an account")
    @PostMapping("/deposit/{number}")
    public ResponseEntity<?> depositToAccount(@PathVariable String number, @RequestBody Double amount) {
        try {
            Account updatedAccount = accountService.depositToAccount(number, amount);
            return ResponseEntity.ok(updatedAccount);
        } catch (IllegalArgumentException e) {
            if (e.getMessage().contains("not found")) {
                log.error("Account with number " + number + " not found", e);
                return ResponseEntity.badRequest().body("Account with number " + number + " not found");
            } else {
                log.error("Amount for deposit must be greater than 0", e);
                return ResponseEntity.badRequest().body("Amount for deposit must be greater than 0");
            }
        }
    }

    @Operation(summary = "Withdraw an amount from an account")
    @PutMapping("/withdraw/{accountNumber}")
    public ResponseEntity<?> withdrawFromAccount(@PathVariable String accountNumber, @RequestBody Double amount) {
        try {
            boolean withdrawalSuccessful = accountService.withdrawFromAccount(accountNumber, amount);
            if (withdrawalSuccessful) {
                return ResponseEntity.ok("Withdrawal successful");
            } else {
                return ResponseEntity.badRequest().body("Insufficient balance");
            }
        } catch (IllegalArgumentException e) {
            log.error("Error withdrawing amount: " + e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "Transfer an amount from one account to another")
    @PutMapping("/transfer/{fromAccountNumber}/{toAccountNumber}")
    public ResponseEntity<?> transferMoney(@PathVariable String fromAccountNumber, @PathVariable String toAccountNumber, @RequestBody double amount) {
        try {
            accountService.transferMoney(fromAccountNumber, toAccountNumber, amount);
            return ResponseEntity.ok("Transfer successful");
        } catch (IllegalArgumentException e) {
            log.error("Error transferring amount: " + e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
