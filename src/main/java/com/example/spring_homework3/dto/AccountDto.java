package com.example.spring_homework3.dto;

import com.example.spring_homework3.domain.Currency;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AccountDto {
    private Currency currency;
    private Double balance;
}
