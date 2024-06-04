package com.example.spring_homework3.mapper.customer;

import com.example.spring_homework3.domain.Customer;
import com.example.spring_homework3.dto.customer.CustomerDtoRequest;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.modelmapper.config.Configuration.AccessLevel.PRIVATE;

@Configuration
public class CustomerDtoMapperRequestConfig {
    @Bean
    public ModelMapper customerDtoRequestMapper() {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT)
                .setFieldMatchingEnabled(true)
                .setSkipNullEnabled(true)
                .setFieldAccessLevel(PRIVATE);

        mapper.createTypeMap(CustomerDtoRequest.class, Customer.class)
                .addMapping(CustomerDtoRequest::getName, Customer::setName)
                .addMapping(CustomerDtoRequest::getEmail, Customer::setEmail)
                .addMapping(CustomerDtoRequest::getAge, Customer::setAge)
                .addMapping(CustomerDtoRequest::getPhoneNumber, Customer::setPhoneNumber)
                .addMapping(CustomerDtoRequest::getPassword, Customer::setPassword);

        return mapper;
    }
}
