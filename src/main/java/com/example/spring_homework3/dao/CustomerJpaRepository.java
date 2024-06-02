package com.example.spring_homework3.dao;

import com.example.spring_homework3.domain.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CustomerJpaRepository extends JpaRepository<Customer, Long> {
    @Query("SELECT c FROM Customer c WHERE c.id BETWEEN :from AND :to")
    Page<Customer> findAllInRange(@Param("from") Integer from, @Param("to") Integer to, Pageable pageable);

    @Modifying
    @Transactional
    @Query("UPDATE Customer c SET c.name = :name, c.email = :email, c.age = :age WHERE c.id = :id")
    void updateCustomerById(@Param("id") Long id,
                            @Param("name") String name,
                            @Param("email") String email,
                            @Param("age") Integer age);
}
