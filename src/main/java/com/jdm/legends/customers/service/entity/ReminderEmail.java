package com.jdm.legends.customers.service.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReminderEmail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime sentTimeEmail;

    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime enterTimeEmail;

    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime deadLineEmail;

    private Long temporaryCustomerId;

    @JsonIgnore
    public boolean isDeadLinePassed(LocalDateTime request) {
        return request.isAfter(deadLineEmail);
    }
}
