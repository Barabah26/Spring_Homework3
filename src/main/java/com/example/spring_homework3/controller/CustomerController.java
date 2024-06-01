package com.example.spring_homework3.controller;

import com.example.spring_homework3.domain.Account;
import com.example.spring_homework3.domain.Customer;
import com.example.spring_homework3.dto.AccountDto;
import com.example.spring_homework3.dto.CustomerDto;
import com.example.spring_homework3.service.DefaultAccountService;
import com.example.spring_homework3.service.DefaultCustomerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customers")
@CrossOrigin(origins = "http://localhost:3000")
@Slf4j
public class CustomerController {
    private final DefaultCustomerService customerService;
    private final DefaultAccountService accountService;
    private final ObjectMapper objectMapper;


    @Autowired
    public CustomerController(DefaultCustomerService customerService, DefaultAccountService accountService, ObjectMapper objectMapper) {
        this.customerService = customerService;
        this.accountService = accountService;
        this.objectMapper = objectMapper;
    }

    @ModelAttribute
    public void configureObjectMapper() {
        SimpleFilterProvider filters = new SimpleFilterProvider();
        filters.addFilter("customerFilter", SimpleBeanPropertyFilter.serializeAll());
        filters.addFilter("accountFilter", SimpleBeanPropertyFilter.filterOutAllExcept("number", "currency", "balance"));
        objectMapper.setFilterProvider(filters);
    }

    @Operation(summary = "Get customer by id")
    @GetMapping("/{id}")
    public ResponseEntity<?> getCustomer(@PathVariable Long id) {
        try {
            customerService.assignAccountsToCustomers();
            return ResponseEntity.ok(customerService.getOne(id));
        } catch (Exception e) {
            log.error("Customer not found with ID " + id, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Customer with ID " + id + " not found");
        }
    }

    @Operation(summary = "Get all customers")
    @GetMapping
    public List<Customer> getAllCustomers() {
        customerService.assignAccountsToCustomers();
        return customerService.findAll();
    }

    @Operation(summary = "Create a new customer")
    @PostMapping
    public Customer createCustomer(@RequestBody CustomerDto customer) {
        Customer newCustomer = new Customer(customer.getName(), customer.getEmail(), customer.getAge(), customer.getPassword(), customer.getPhoneNumber());
        return customerService.save(newCustomer);
    }

    @Operation(summary = "Update customer")
    @PutMapping("/id/{id}")
    public ResponseEntity<?> updateCustomer(@PathVariable Long id, @RequestBody CustomerDto customerDto) {
        try {
            Customer currentCustomer = customerService.getOne(id);
            if (customerDto.getName() != null) {
                currentCustomer.setName(customerDto.getName());
            }
            if (customerDto.getEmail() != null) {
                currentCustomer.setEmail(customerDto.getEmail());
            }
            if (customerDto.getAge() != null) {
                currentCustomer.setAge(customerDto.getAge());
            }

            Customer updatedCustomer = customerService.update(id, currentCustomer);

            if (updatedCustomer != null) {
                return ResponseEntity.ok(updatedCustomer);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Customer with ID " + id + " not found");
            }
        } catch (IllegalArgumentException e) {
            log.error("Customer not found with ID " + id, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Customer with ID " + id + " not found");
        } catch (RuntimeException e) {
            log.error("An error occurred while updating the customer with ID " + id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while updating the customer");
        }
    }

    @Operation(summary = "Delete a customer by its ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCustomer(@PathVariable Long id) {
        try {
            Customer customer = customerService.getOne(id);
            customerService.deleteCustomerAccounts(customer);
            customerService.deleteById(customer.getId());
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            log.error("Customer not found with ID " + id, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Customer with ID " + id + " not found");
        }
    }


    @PostMapping("/{customerId}/accounts")
    public ResponseEntity<?> createAccountForCustomer(@PathVariable Long customerId, @RequestBody AccountDto accountDto) {
        try {
            Customer customer = customerService.getOne(customerId);
            customerService.createAccountForCustomer(customer.getId(), accountDto.getCurrency(), accountDto.getBalance());
            customerService.update(customerId, customer);
            return ResponseEntity.ok(customer);
        } catch (RuntimeException e) {
            log.error("Customer not found with ID " + customerId, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Customer with ID " + customerId + " not found");
        }
    }

    @Operation(summary = "Delete an account from a customer by its ID")
    @DeleteMapping("/{customerId}/accounts/{accountNumber}")
    public ResponseEntity<?> deleteAccountFromCustomer(@PathVariable Long customerId, @PathVariable String accountNumber) {
        try {
            Customer customer = customerService.getOne(customerId);
            Account accountToDelete = null;
            for (Account account : customer.getAccounts()) {
                if (account.getNumber().equals(accountNumber)) {
                    accountToDelete = account;
                    break;
                }
            }
            if (accountToDelete != null) {
                customerService.deleteAccountFromCustomer(customer.getId(), accountToDelete.getNumber());
                customerService.update(customerId, customer);
                return ResponseEntity.ok("Account successfully deleted");
            } else {
                return ResponseEntity.badRequest().body("Account with number " + accountNumber + " not found");
            }
        } catch (IllegalArgumentException e) {
            log.error("Customer not found with ID " + customerId, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Customer with ID " + customerId + " not found");
        }
    }
}
