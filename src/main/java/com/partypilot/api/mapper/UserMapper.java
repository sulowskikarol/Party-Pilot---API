package com.partypilot.api.mapper;

import com.partypilot.api.dto.SignUpDto;
import com.partypilot.api.dto.UserDto;
import com.partypilot.api.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto toUserDto(User user);

    @Mapping(target = "password", ignore = true)
    User signUpToUser(SignUpDto signUpDto);
}
