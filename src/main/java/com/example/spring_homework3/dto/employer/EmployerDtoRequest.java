package com.example.spring_homework3.dto.employer;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class EmployerDtoRequest {
    @Size(min = 3, message = "Name must be at least 3 characters long")
    private String name;
    @Size(min = 3, message = "Address must be at least 2 characters long")
    private String address;
}
