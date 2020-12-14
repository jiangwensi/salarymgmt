package com.example.salarymgt.service;

import com.example.salarymgt.dto.UserDto;
import com.example.salarymgt.exception.InvalidInputException;
import com.example.salarymgt.request.UserRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Jiang Wensi on 14/12/2020
 */
@Service
public class UserServiceImpl implements UserService {

    @Override
    public List<UserDto> uploadUsers(List<UserRequest> userRequests) throws InvalidInputException {
        return null;
    }

    @Override
    public List<UserDto> fetchUsers(BigDecimal minSalary, BigDecimal maxSalary, Integer start, Integer limit) throws InvalidInputException {
        return null;
    }


    @Override
    public UserDto getUser(String userID) throws InvalidInputException {
        return null;
    }

    @Override
    public UserDto createUser(UserRequest userRequest) throws InvalidInputException {
        return null;
    }

    @Override
    public UserDto updateUser(UserRequest userRequest) throws InvalidInputException {
        return null;
    }

    @Override
    public void deleteUser(String userId) throws InvalidInputException {

    }
}
