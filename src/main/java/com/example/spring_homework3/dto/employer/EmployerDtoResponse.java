package com.example.spring_homework3.dto.employer;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class EmployerDtoResponse {
    private Long id;
    private String name;
    private String address;
    private List<String> customersNames;
    private LocalDateTime creationDate;
    private LocalDateTime lastModifiedDate;
}
