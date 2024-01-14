package com.jdm.legends.customers.service.mapping;

import com.jdm.legends.customers.controller.dto.CustomerRequest;
import com.jdm.legends.customers.service.entity.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import com.jdm.legends.customers.service.enums.Roles;


@Mapper
public interface CustomerMapper {
    CustomerMapper INSTANCE = Mappers.getMapper(CustomerMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "role", expression = "java(getRole(request.role()))")
    Customer customerRequestToCustomerEntity(CustomerRequest request);

    default Roles getRole(Roles roles) {
        return Roles.getRole(roles.name());
    }
}
