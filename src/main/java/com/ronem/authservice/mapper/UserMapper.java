/**
 * Author: Ram Mandal
 * Created on @System: Apple M1 Pro
 * User:rammandal
 * Date:28/01/2026
 * Time:10:27
 */


package com.ronem.authservice.mapper;

import com.ronem.authservice.model.entity.User;
import com.ronem.authservice.model.dto.request.CreateUserRequest;
import com.ronem.authservice.model.dto.response.CreateUserResponse;
import com.ronem.authservice.model.dto.UserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "userId", source = "user.id")
    CreateUserResponse toResponse(User user);

    @Mapping(target = "userRole", source = "user.userRole")
    @Mapping(target = "status", source = "user.status")
    UserDTO toUserDTO(User user);

    @Mapping(target = "userRole", source = "user.userRole")
    @Mapping(target = "status", source = "user.status")
    List<UserDTO> toUserDTO(List<User> users);

    User toEntity(CreateUserRequest request);
}