package com.example.spring_homework3.dto.account;

import com.example.spring_homework3.domain.Currency;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AccountDtoResponse {
    private Long id;
    private UUID number;
    private Currency currency;
    private double balance;
    private String customerName;
    private LocalDateTime creationDate;
    private LocalDateTime lastModifiedDate;

}
