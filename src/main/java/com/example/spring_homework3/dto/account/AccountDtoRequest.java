package com.example.spring_homework3.dto.account;

import com.example.spring_homework3.domain.Currency;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountDtoRequest {
    @NotNull(message = "Currency must be provided")
    @Enumerated(EnumType.STRING)
    private Currency currency;

    @NotNull(message = "Balance must be provided, it can be negative")
    @Digits(integer = 12, message = "Balance must be a valid monetary amount with up to 12 integer digits", fraction = 0)
    private double balance;
}
