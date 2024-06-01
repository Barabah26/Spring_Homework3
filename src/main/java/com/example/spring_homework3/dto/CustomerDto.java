package com.example.spring_homework3.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CustomerDto {
    private String name;
    private String email;
    private Integer age;
    private String password;
    private String phoneNumber;

}
