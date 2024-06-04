package com.example.spring_homework3.dto.customer;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CustomerDtoResponse {
    @JsonView(CustomerView.Summary.class)
    private Long id;

    @JsonView(CustomerView.Summary.class)
    private String name;

    @JsonView(CustomerView.Detailed.class)
    private String email;

    @JsonView(CustomerView.Detailed.class)
    private Integer age;

    @JsonView(CustomerView.Summary.class)
    private Set<UUID> accountNumbers;

    @JsonView(CustomerView.Detailed.class)
    private List<String> employerNames;

    @JsonView(CustomerView.Detailed.class)
    private String phoneNumber;

    @JsonView(CustomerView.Detailed.class)
    private LocalDateTime creationDate;

    @JsonView(CustomerView.Detailed.class)
    private LocalDateTime lastModifiedDate;
}
