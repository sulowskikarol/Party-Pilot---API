package com.partypilot.api.mapper;

import com.partypilot.api.dto.EventDto;
import com.partypilot.api.dto.EventShortDto;
import com.partypilot.api.model.Event;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = { CommentMapper.class })
public interface EventMapper {
    EventShortDto toEventShortDto(Event event);

    @Mapping(target = "comments", ignore = true)
    EventDto toEventDto(Event event);
}
