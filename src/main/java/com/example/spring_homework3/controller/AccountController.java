package com.example.spring_homework3.controller;


import com.example.spring_homework3.domain.Account;
import com.example.spring_homework3.dto.account.AccountDtoResponse;
import com.example.spring_homework3.mapper.account.AccountDtoMapperResponse;
import com.example.spring_homework3.service.DefaultAccountService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import io.swagger.models.auth.In;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/accounts")
@CrossOrigin(origins = "http://localhost:3000")
@Slf4j
@RequiredArgsConstructor
public class AccountController {
    private final DefaultAccountService accountService;
    private final AccountDtoMapperResponse accountDtoMapperResponse;

    @Operation(summary = "Get all accounts")
    @GetMapping("/all")
    public List<AccountDtoResponse> getAllAccounts() {
        return accountService.findAll().stream().map(accountDtoMapperResponse::convertToDto).toList();
    }

    @DeleteMapping("/{id}")
    public void deleteAccountById(@PathVariable Long id) {
        accountService.deleteById(id);
    }

    @Operation(summary = "Get account by id")
    @GetMapping("/{id}")
    public ResponseEntity<?> getAccountById(@PathVariable Long id) {
        try {
            Account account = accountService.getOne(id).get();
            AccountDtoResponse accountDtoResponse = accountDtoMapperResponse.convertToDto(account);
            return ResponseEntity.ok(accountDtoResponse);
        } catch (RuntimeException e) {
            log.error("Account with id " + id + " not found", e);
            return ResponseEntity.badRequest().body("Account with id " + id + " not found");
        }
    }

    @Operation(summary = "Deposit an amount to an account")
    @PostMapping("/deposit/{number}")
    public ResponseEntity<?> depositToAccount(@PathVariable UUID number, @RequestBody Double amount) {
        try {
            Account updatedAccount = accountService.depositToAccount(number, amount);
            AccountDtoResponse accountDtoResponse = accountDtoMapperResponse.convertToDto(updatedAccount);
            return ResponseEntity.ok(accountDtoResponse);
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
    public ResponseEntity<?> withdrawFromAccount(@PathVariable UUID accountNumber, @RequestBody Double amount) {
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
    public ResponseEntity<?> transferMoney(@PathVariable UUID fromAccountNumber, @PathVariable UUID toAccountNumber, @RequestBody double amount) {
        try {
            accountService.transferMoney(fromAccountNumber, toAccountNumber, amount);
            return ResponseEntity.ok("Transfer successful");
        } catch (IllegalArgumentException e) {
            log.error("Error transferring amount: " + e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
