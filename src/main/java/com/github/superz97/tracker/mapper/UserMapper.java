package com.github.superz97.tracker.mapper;

import com.github.superz97.tracker.dto.response.UserResponse;
import com.github.superz97.tracker.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserResponse toUserResponse(User user);

}
