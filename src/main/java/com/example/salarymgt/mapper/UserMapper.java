package com.example.salarymgt.mapper;

import com.example.salarymgt.dto.UserDto;
import com.example.salarymgt.entity.UserEntity;
import com.example.salarymgt.request.UserRequest;
import com.example.salarymgt.response.UserResponse;
import org.mapstruct.Mapper;

/**
 * Created by Jiang Wensi on 14/12/2020
 */
@Mapper
public interface UserMapper {
    UserEntity mapUserRequestToEntity(UserRequest userRequest);
    UserEntity mapUserDtoToEntity(UserDto userDto);
    UserDto mapUserEntityToDto(UserEntity userEntity);
    UserDto mapUserResponseToDto(UserResponse userResponse);
    UserResponse mapUserDtoToUserResponse(UserDto userDto);
}
