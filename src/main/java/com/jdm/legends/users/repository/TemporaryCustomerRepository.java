package com.jdm.legends.users.repository;

import com.jdm.legends.users.service.entity.TemporaryCustomer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface TemporaryCustomerRepository extends JpaRepository<TemporaryCustomer, Long> {
    @Query(value = """
                SELECT ts.user_name AS userName, MAX(hb.bid_value) AS bidValue
                FROM temporary_customer_history_bid tshb
                         INNER JOIN history_bid hb ON tshb.history_bid_id = hb.id
                         INNER JOIN temporary_customer ts ON tshb.temporary_customer_id = ts.id
                         INNER JOIN cars c ON hb.car_id = c.id
                WHERE c.id = ?1
                GROUP BY ts.id
                LIMIT 1;
            """, nativeQuery = true)
    Optional<WinnerUser> getWinnerUser(Long carId);
}
