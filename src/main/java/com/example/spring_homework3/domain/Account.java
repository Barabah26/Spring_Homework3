package com.example.spring_homework3.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "accounts")
@NoArgsConstructor
@Getter
@Setter
public class Account extends AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String number;
    @Enumerated(EnumType.STRING)
    private Currency currency;
    private Double balance;
    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    public Account(Currency currency, Customer customer) {
        this.currency = currency;
        this.customer = customer;
        this.number = UUID.randomUUID().toString();
        this.balance = 0D;
    }

    public Account(Long id, String number, Currency currency, Double balance, Customer customer) {
        this.id = id;
        this.number = number;
        this.currency = currency;
        this.balance = balance;
        this.customer = customer;
    }


    @Override
    public String toString() {
        return "Account{" +
                ", number='" + number + '\'' +
                ", currency=" + currency +
                ", customer=" + customer +
                ", balance=" + balance +
                '}';
    }
}
