package com.example.spring_homework3.dao;


import com.example.spring_homework3.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface AccountJpaRepository extends JpaRepository<Account, Long> {
    Account findByNumber(String number);
    @Modifying
    @Transactional
    @Query("UPDATE Account a SET a.balance = :balance WHERE a.number = :number")
    void updateBalanceByNumber(@Param("number") String number, @Param("balance") Double balance);
}
