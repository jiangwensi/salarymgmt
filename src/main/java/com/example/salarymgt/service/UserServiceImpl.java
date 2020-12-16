package com.example.salarymgt.service;

import com.example.salarymgt.dto.UserDto;
import com.example.salarymgt.entity.UserEntity;
import com.example.salarymgt.exception.InvalidInputException;
import com.example.salarymgt.mapper.UserMapper;
import com.example.salarymgt.repo.UserRepository;
import com.example.salarymgt.request.UserRequest;
import com.example.salarymgt.util.NumUtil;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by Jiang Wensi on 14/12/2020
 */
@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private UserMapper userMapper;

    @Override
    public List<UserDto> uploadUsers(List<UserRequest> userRequests) {

        List<UserDto> result = new ArrayList<>();
        if (userRequests == null || userRequests.size() == 0) {
            return result;
        }
        List<UserEntity> userEntities = userRequests.stream().map(r -> userMapper.mapUserRequestToEntity(r)).collect(Collectors.toList());
        List<UserEntity> savedEntities = userRepository.saveAll(userEntities);
        result = savedEntities.stream().map(e -> userMapper.mapUserEntityToDto(e)).collect(Collectors.toList());
        return result;
    }

    @Override
    public List<UserDto> fetchUsers(BigDecimal minSalary, BigDecimal maxSalary, Integer offset, Integer limit) {

        List<UserEntity> userEntities;
        if (minSalary == null) {
            minSalary = NumUtil.bigDecimal(0);
        }
        if (maxSalary == null) {
            maxSalary = NumUtil.bigDecimal(4000);
        }
        if (offset == null || offset < 0) {
            offset = 0;
        }
        if (limit == null || limit == 0) {
            userEntities = userRepository.findBySalaryRangeOffset(minSalary, maxSalary, offset);
        } else {
            userEntities = userRepository.findBySalaryRangeOffsetLimit(minSalary, maxSalary, offset, limit);
        }

        List<UserDto> result = userEntities.stream().map(e -> userMapper.mapUserEntityToDto(e)).collect(Collectors.toList());

        return result;
    }


    @Override
    public UserDto getUser(String userID) {
        if (userID == null) {
            return null;
        }
        UserEntity userEntity = userRepository.findById(userID);
        UserDto userDto = userMapper.mapUserEntityToDto(userEntity);

        return userDto;
    }

    @Override
    public UserDto createUser(UserRequest userRequest) {
        if (userRepository.findById(userRequest.getId()) != null) {
            throw new InvalidInputException("Employee ID already exists");
        }
        if (userRepository.findByLogin(userRequest.getLogin()) != null) {
            throw new InvalidInputException("Employee login not unique");
        }

        UserEntity userEntity = userMapper.mapUserRequestToEntity(userRequest);
        UserEntity updatedUserEntity = userRepository.save(userEntity);
        UserDto result = userMapper.mapUserEntityToDto(updatedUserEntity);
        return result;
    }

    @Override
    public UserDto updateUser(UserRequest userRequest) {
        UserEntity userEntity = userRepository.findById(userRequest.getId());
        if (userEntity == null) {
            throw new InvalidInputException("No such employee");
        }
        if (!userRequest.getLogin().equals(userEntity.getLogin())) {
            if (userRepository.findByLogin(userRequest.getLogin()) != null) {
                throw new InvalidInputException("Employee login not unique");
            }
        }
        UserEntity updatedUserEntity = userMapper.mapUserRequestToEntity(userRequest);
        UserEntity savedUpdatedUserEntity = userRepository.save(updatedUserEntity);
        UserDto result = userMapper.mapUserEntityToDto(savedUpdatedUserEntity);
        return result;
    }

    @Override
    @Transactional
    public void deleteUser(String userId) {
        UserEntity userEntity = userRepository.findById(userId);
        if (userEntity == null) {
            throw new InvalidInputException("No such employee");
        }
        userRepository.deleteById(userId);

    }
}
