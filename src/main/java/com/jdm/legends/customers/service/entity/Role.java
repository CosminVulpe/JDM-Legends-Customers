package com.jdm.legends.customers.service.entity;

import com.jdm.legends.customers.service.enums.RolesType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ElementCollection(targetClass = RolesType.class)
    @Enumerated(EnumType.STRING)
    private List<RolesType> rolesType;

    public Role(List<RolesType> rolesType) {
        this.rolesType = rolesType;
    }
}
