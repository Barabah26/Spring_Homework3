package com.example.spring_homework3.controller;

import com.example.spring_homework3.domain.Account;
import com.example.spring_homework3.domain.Customer;
import com.example.spring_homework3.dto.account.AccountDtoRequest;
import com.example.spring_homework3.dto.customer.CustomerDtoRequest;
import com.example.spring_homework3.dto.customer.CustomerDtoResponse;
import com.example.spring_homework3.dto.customer.CustomerView;
import com.example.spring_homework3.mapper.account.AccountDtoMapperRequest;
import com.example.spring_homework3.mapper.customer.CustomerDtoMapperRequest;
import com.example.spring_homework3.mapper.customer.CustomerDtoMapperResponse;
import com.example.spring_homework3.service.DefaultAccountService;
import com.example.spring_homework3.service.DefaultCustomerService;
import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/customers")
@CrossOrigin(origins = "http://localhost:3000")
@Slf4j
@RequiredArgsConstructor
public class CustomerController {
    private final DefaultCustomerService customerService;
    private final DefaultAccountService accountService;
    private final CustomerDtoMapperResponse customerDtoMapperResponse;
    private final CustomerDtoMapperRequest customerDtoMapperRequest;
    private final AccountDtoMapperRequest accountDtoMapperRequest;

    @Operation(summary = "Get customer by id")
    @GetMapping("/{id}")
    public ResponseEntity<?> getCustomer(@PathVariable Long id) {
        try {
            customerService.assignAccountsToCustomers();
            return ResponseEntity.ok(customerDtoMapperRequest.convertToDto(customerService.getOne(id).get()));
        } catch (Exception e) {
            log.error("Customer not found with ID " + id, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Customer with ID " + id + " not found");
        }
    }

    @Operation(summary = "Get all customers")
    @JsonView(CustomerView.Summary.class)
    @GetMapping
    public ResponseEntity<?> getAllCustomers(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {

        Page<CustomerDtoResponse> customersPage = customerService.findAll(0, Integer.MAX_VALUE, PageRequest.of(page, size)).map(customerDtoMapperResponse::convertToDto);
        List<CustomerDtoResponse> customers = customersPage.getContent();

        return ResponseEntity.ok().body(customers);
    }


    @Operation(summary = "Create a new customer")
    @PostMapping
    public ResponseEntity<?> createCustomer(@RequestBody CustomerDtoRequest customer) {
        Customer newCustomer = customerDtoMapperRequest.convertToEntity(customer);
        try {
            customerService.save(newCustomer);
            return ResponseEntity.ok(customerDtoMapperResponse.convertToDto(newCustomer));
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
//        Customer newCustomer = new Customer(customer.getName(), customer.getEmail(), customer.getAge(), customer.getPassword(), customer.getPhoneNumber());
//        return customerService.save(newCustomer);
    }

    @Operation(summary = "Update customer")
    @PutMapping("/id/{id}")
    public ResponseEntity<?> updateCustomer(@PathVariable Long id, @RequestBody CustomerDtoRequest customerDto) {
        try {
            Customer updatedCustomer = customerDtoMapperRequest.convertToEntity(customerDto);
            updatedCustomer.setId(id);
            Customer currentCustomer = customerService.update(id, updatedCustomer);
            if (customerDto.getName() != null) {
                currentCustomer.setName(customerDto.getName());
            }
            if (customerDto.getEmail() != null) {
                currentCustomer.setEmail(customerDto.getEmail());
            }
            if (customerDto.getAge() >= 18) {
                currentCustomer.setAge(customerDto.getAge());
            }
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
            Customer customer = customerService.getOne(id).orElseThrow();
            customerService.deleteCustomerAccounts(customer);
            customerService.deleteById(customer.getId());
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            log.error("Customer not found with ID " + id, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Customer with ID " + id + " not found");
        }
    }



    @PostMapping("/{customerId}/accounts")
    public ResponseEntity<?> createAccountForCustomer(@PathVariable Long customerId, @RequestBody AccountDtoRequest accountDto) {
        try {
            Customer customer = customerService.getOne(customerId).get();
            Account account = accountDtoMapperRequest.convertToEntity(accountDto);
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
    public ResponseEntity<?> deleteAccountFromCustomer(@PathVariable Long customerId, @PathVariable UUID accountNumber) {
        try {
            Customer customer = customerService.getOne(customerId).orElseThrow();
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
