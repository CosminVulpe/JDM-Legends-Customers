package com.jdm.legends.customers.repository;

import com.jdm.legends.customers.service.entity.TemporaryCustomer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TemporaryCustomerRepository extends JpaRepository<TemporaryCustomer, Long> {
}
