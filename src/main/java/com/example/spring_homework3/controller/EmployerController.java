package com.example.spring_homework3.controller;

import com.example.spring_homework3.domain.Employer;
import com.example.spring_homework3.service.DefaultEmployerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employer")
@CrossOrigin(origins = "http://localhost:3000")
@Slf4j
@RequiredArgsConstructor
public class EmployerController {

    private final DefaultEmployerService employerService;
    private final ObjectMapper objectMapper;

    @ModelAttribute
    public void configureObjectMapper() {
        SimpleFilterProvider filters = new SimpleFilterProvider();
        filters.addFilter("customerFilter", SimpleBeanPropertyFilter.serializeAll());
        filters.addFilter("accountFilter", SimpleBeanPropertyFilter.filterOutAllExcept("number", "currency", "balance"));
        objectMapper.setFilterProvider(filters);
    }

    @Operation(summary = "Get all employers")
    @GetMapping
    public List<Employer> getAllEmployers() {
        return employerService.findAll();
    }


}
