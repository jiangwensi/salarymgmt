package com.example.salarymgt.service;

import com.example.salarymgt.dto.UserDto;
import com.example.salarymgt.exception.InvalidInputException;
import com.example.salarymgt.request.UserRequest;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Jiang Wensi on 11/12/2020
 */
public interface UserService {

    List<UserDto> uploadUsers(List<UserRequest> userRequests);

    List<UserDto> fetchUsers(BigDecimal minSalary, BigDecimal maxSalary, Integer offset, Integer limit);

    UserDto getUser(String userID);

    UserDto createUser(UserRequest userRequest);

    UserDto updateUser(UserRequest userRequest);

    void deleteUser(String userId);
}
