package com.example.spring_homework3.mapper.employer;

import com.example.spring_homework3.domain.Customer;
import com.example.spring_homework3.domain.Employer;
import com.example.spring_homework3.dto.employer.EmployerDtoResponse;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;
import java.util.stream.Collectors;

import static org.modelmapper.config.Configuration.AccessLevel.PRIVATE;

@Configuration
public class EmployerDtoMapperResponseConfig {

    @Bean
    public ModelMapper employerDtoResponseMapper() {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT)
                .setFieldMatchingEnabled(true)
                .setSkipNullEnabled(true)
                .setFieldAccessLevel(PRIVATE);

        mapper.createTypeMap(Employer.class, EmployerDtoResponse.class)
                .addMapping(Employer::getId, EmployerDtoResponse::setId)
                .addMapping(Employer::getName, EmployerDtoResponse::setName)
                .addMapping(Employer::getAddress, EmployerDtoResponse::setAddress)
                .addMapping(src -> {
                    if (src.getCustomers() != null) {
                        return src.getCustomers().stream().map(Customer::getName).collect(Collectors.toList());
                    }
                    return Collections.emptyList();
                }, EmployerDtoResponse::setCustomersNames)
                .addMapping(Employer::getCreationDate, EmployerDtoResponse::setCreationDate)
                .addMapping(Employer::getLastModifiedDate, EmployerDtoResponse::setLastModifiedDate)
        ;

        return mapper;
    }
}
