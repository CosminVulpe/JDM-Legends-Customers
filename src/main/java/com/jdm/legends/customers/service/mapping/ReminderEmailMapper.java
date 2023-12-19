package com.jdm.legends.customers.service.mapping;

import com.jdm.legends.customers.controller.dto.ReminderEmailDTO;
import com.jdm.legends.customers.service.entity.ReminderEmail;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ReminderEmailMapper {
    ReminderEmailMapper INSTANCE = Mappers.getMapper(ReminderEmailMapper.class);

    ReminderEmailDTO remainderEmailEntityToRemainderEmailDTO(ReminderEmail reminderEmail);
}
