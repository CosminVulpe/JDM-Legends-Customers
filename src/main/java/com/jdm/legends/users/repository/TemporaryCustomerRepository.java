package com.jdm.legends.users.repository;

import com.jdm.legends.users.service.entity.TemporaryCustomer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface TemporaryCustomerRepository extends JpaRepository<TemporaryCustomer, Long> {
}
