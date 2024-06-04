package com.example.spring_homework3.mapper.account;

import com.example.spring_homework3.domain.Account;
import com.example.spring_homework3.dto.account.AccountDtoRequest;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.modelmapper.config.Configuration.AccessLevel.PRIVATE;

@Configuration
public class AccountDtoMapperRequestConfig {
    @Bean
    public ModelMapper accountDtoRequestMapper() {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT)
                .setFieldMatchingEnabled(true)
                .setSkipNullEnabled(true)
                .setFieldAccessLevel(PRIVATE);

        mapper.createTypeMap(AccountDtoRequest.class, Account.class)
                .addMapping(AccountDtoRequest::getCurrency, Account::setCurrency)
                .addMapping(AccountDtoRequest::getBalance, Account::setBalance);

        return mapper;
    }
}
