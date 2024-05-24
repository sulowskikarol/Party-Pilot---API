package com.partypilot.api.mapper;

import com.partypilot.api.dto.EventShortDto;
import com.partypilot.api.model.Event;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EventMapper {
    EventShortDto toEventShortDto(Event event);
}
