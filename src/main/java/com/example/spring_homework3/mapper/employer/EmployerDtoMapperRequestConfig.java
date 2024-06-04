package com.example.spring_homework3.mapper.employer;

import com.example.spring_homework3.domain.Employer;
import com.example.spring_homework3.dto.employer.EmployerDtoRequest;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.modelmapper.config.Configuration.AccessLevel.PRIVATE;

@Configuration
public class EmployerDtoMapperRequestConfig {
    @Bean
    public ModelMapper employerDtoRequestMapper() {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT)
                .setFieldMatchingEnabled(true)
                .setSkipNullEnabled(true)
                .setFieldAccessLevel(PRIVATE);

        mapper.createTypeMap(EmployerDtoRequest.class, Employer.class)
                .addMapping(EmployerDtoRequest::getName, Employer::setName)
                .addMapping(EmployerDtoRequest::getAddress, Employer::setAddress);

        return mapper;
    }
}
