package com.jdm.legends.customers.service.mapping;

import com.jdm.legends.customers.controller.dto.TemporaryCustomerDTO;
import com.jdm.legends.customers.controller.dto.TemporaryCustomerRequest;
import com.jdm.legends.customers.service.entity.TemporaryCustomer;
import com.jdm.legends.customers.service.enums.Roles;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TemporaryCustomerMapper {
    TemporaryCustomerMapper INSTANCE = Mappers.getMapper(TemporaryCustomerMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "historyBidId", ignore = true)
    @Mapping(target = "role", expression = "java(mapRole(request))")
    TemporaryCustomer tempCustomerRequestToTempCustomerEntity(TemporaryCustomerRequest request);

    @Mapping(target = "historyBidId", ignore = true)
    TemporaryCustomerDTO tempCustomerToTempCustomerDTO(TemporaryCustomer temporaryCustomer);


    default String mapRole(TemporaryCustomerRequest request) {
        return request.checkInformationStoredTemporarily()
                ? Roles.POTENTIAL_CLIENT.getValue()
                : Roles.ANONYMOUS.getValue();
    }
}
