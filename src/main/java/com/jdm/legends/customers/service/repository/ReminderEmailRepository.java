package com.jdm.legends.customers.service.repository;

import com.jdm.legends.customers.service.entity.ReminderEmail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReminderEmailRepository extends JpaRepository<ReminderEmail, Long> {
}
