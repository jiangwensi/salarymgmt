package com.example.salarymgt.service;

import com.example.salarymgt.dto.UserDto;
import com.example.salarymgt.entity.UserEntity;
import com.example.salarymgt.exception.InvalidInputException;
import com.example.salarymgt.mapper.UserMapper;
import com.example.salarymgt.repo.UserRepository;
import com.example.salarymgt.request.UserRequest;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@SpringBootTest
@AllArgsConstructor
@NoArgsConstructor
class UserServiceImplTest {

    @MockBean
    UserRepository userRepository;

    @MockBean
    UserMapper userMapper;

    @Autowired
    UserService userService;

    @BeforeEach
    void setUp() {

    }

    @Test
    void uploadUsers() {
        List<UserRequest> userRequests = new ArrayList<>();
        userRequests.add(testUserRequest(1));
        userRequests.add(testUserRequest(2));
        userRequests.add(testUserRequest(3));

        List<UserEntity> userEntities = new ArrayList<>();
        userEntities.add(testUserEntity(1));
        userEntities.add(testUserEntity(2));
        userEntities.add(testUserEntity(3));

        List<UserDto> expectedDtos = new ArrayList<>();
        expectedDtos.add(testUserDto(1));
        expectedDtos.add(testUserDto(2));
        expectedDtos.add(testUserDto(3));

        given(userRepository.saveAll(any())).willReturn(userEntities);

        given(userMapper.mapUserRequestToEntity(testUserRequest(1))).willReturn(testUserEntity(1));
        given(userMapper.mapUserRequestToEntity(testUserRequest(2))).willReturn(testUserEntity(2));
        given(userMapper.mapUserRequestToEntity(testUserRequest(3))).willReturn(testUserEntity(3));

        given(userMapper.mapUserEntityToDto(testUserEntity(1))).willReturn(testUserDto(1));
        given(userMapper.mapUserEntityToDto(testUserEntity(2))).willReturn(testUserDto(2));
        given(userMapper.mapUserEntityToDto(testUserEntity(3))).willReturn(testUserDto(3));

        List<UserDto> userDtos = userService.uploadUsers(userRequests);

        verify(userRepository, times(1)).saveAll(any());
        verify(userMapper, times(3)).mapUserEntityToDto(any());
        verify(userMapper, times(3)).mapUserRequestToEntity(any());

        assertEquals(3, userDtos.size());
        assertEquals(expectedDtos, userDtos);
    }

    @Test
    void uploadUsersEmpty() {
        List<UserRequest> userRequests = new ArrayList<>();
        List<UserDto> userDtos = userService.uploadUsers(userRequests);
        verify(userRepository, times(0));
        assertEquals(0, userDtos.size());
    }

    @Test
    void uploadUsersNull() {
        List<UserRequest> userRequests = null;
        List<UserDto> userDtos = userService.uploadUsers(userRequests);
        verify(userRepository, times(0));
        assertEquals(0, userDtos.size());
    }

    @Test
    void fetchUsersAllParam() {
        BigDecimal minSalary = new BigDecimal(1000);
        BigDecimal maxSalary = new BigDecimal(2000);
        Integer offset = 10;
        Integer limit = 3;

        List<UserEntity> userEntities = new ArrayList<>();
        userEntities.add(testUserEntity(1111));
        userEntities.add(testUserEntity(1222));
        userEntities.add(testUserEntity(1333));

        List<UserDto> expectedDtos = new ArrayList<>();
        expectedDtos.add(testUserDto(1111));
        expectedDtos.add(testUserDto(1222));
        expectedDtos.add(testUserDto(1333));

        given(userMapper.mapUserEntityToDto(testUserEntity(1111))).willReturn(testUserDto(1111));
        given(userMapper.mapUserEntityToDto(testUserEntity(1222))).willReturn(testUserDto(1222));
        given(userMapper.mapUserEntityToDto(testUserEntity(1333))).willReturn(testUserDto(1333));

        given(userRepository.findBySalaryRangeOffsetLimit(minSalary, maxSalary, offset, limit)).willReturn(userEntities);
        List<UserDto> userDtos = userService.fetchUsers(minSalary, maxSalary, offset, limit);
        verify(userRepository, times(1)).findBySalaryRangeOffsetLimit(minSalary, maxSalary, offset, limit);
        assertEquals(expectedDtos, userDtos);

    }

    @Test
    void fetchUsersNoParam() {
        BigDecimal minSalary = new BigDecimal(0);
        BigDecimal maxSalary = new BigDecimal(0);
        Integer offset = 0;

        List<UserEntity> userEntities = new ArrayList<>();
        userEntities.add(testUserEntity(1111));
        userEntities.add(testUserEntity(1222));
        userEntities.add(testUserEntity(1333));

        List<UserDto> expectedDtos = new ArrayList<>();
        expectedDtos.add(testUserDto(1111));
        expectedDtos.add(testUserDto(1222));
        expectedDtos.add(testUserDto(1333));

        given(userMapper.mapUserEntityToDto(testUserEntity(1111))).willReturn(testUserDto(1111));
        given(userMapper.mapUserEntityToDto(testUserEntity(1222))).willReturn(testUserDto(1222));
        given(userMapper.mapUserEntityToDto(testUserEntity(1333))).willReturn(testUserDto(1333));

        given(userRepository.findBySalaryRangeOffset(minSalary, maxSalary, offset)).willReturn(userEntities);
        List<UserDto> userDtos = userService.fetchUsers(null, null, null, null);
        verify(userRepository, times(1)).findBySalaryRangeOffset(minSalary, maxSalary, offset);
        assertEquals(expectedDtos, userDtos);
    }

    @Test
    void getUser() {
        UserEntity userEntity1 = testUserEntity(1);
        UserDto expectedUserDto = testUserDto(1);

        given(userRepository.findById(userEntity1.getId())).willReturn(userEntity1);
        given(userMapper.mapUserEntityToDto(userEntity1)).willReturn(expectedUserDto);

        UserDto userDto = userService.getUser(userEntity1.getId());

        verify(userRepository, times(1)).findById(anyString());
        verify(userMapper, times(1)).mapUserEntityToDto(any());
        assertEquals(expectedUserDto, userDto);
    }

    @Test
    void createUser() {
        UserRequest userRequest1 = testUserRequest(1);
        UserEntity userEntity1 = testUserEntity(1);
        UserDto expectedUserDto = testUserDto(1);

        given(userMapper.mapUserRequestToEntity(userRequest1)).willReturn(userEntity1);
        given(userRepository.findById(anyString())).willReturn(null);
        given(userRepository.findByLogin(anyString())).willReturn(null);
        given(userRepository.save(userEntity1)).willReturn(userEntity1);
        given(userMapper.mapUserEntityToDto(userEntity1)).willReturn(expectedUserDto);

        UserDto userDto = userService.createUser(userRequest1);

        verify(userMapper, times(1)).mapUserRequestToEntity(any());
        verify(userRepository, times(1)).findById(anyString());
        verify(userRepository, times(1)).findByLogin(anyString());
        verify(userRepository, times(1)).save(any());
        verify(userMapper, times(1)).mapUserEntityToDto(any());

        assertEquals(expectedUserDto, userDto);
    }

    @Test
    void createUserDuplicateID() {
        UserRequest userRequest1 = testUserRequest(1);
        UserEntity userEntity1 = testUserEntity(1);

        given(userMapper.mapUserRequestToEntity(userRequest1)).willReturn(userEntity1);
        given(userRepository.findById(anyString())).willReturn(userEntity1);

        InvalidInputException exception = assertThrows(InvalidInputException.class,
                () -> userService.createUser(userRequest1));

        assertEquals("Employee ID already exists",exception.getMessage());
        verify(userMapper, times(1)).mapUserRequestToEntity(any());
        verify(userRepository, times(1)).findById(anyString());
        verify(userRepository, times(1));
        verify(userRepository, times(0)).save(any());
        verify(userMapper, times(0)).mapUserEntityToDto(any());

    }



    @Test
    void createUserDuplicateLogin() {
        UserRequest userRequest1 = testUserRequest(1);
        UserEntity userEntity1 = testUserEntity(1);

        given(userMapper.mapUserRequestToEntity(userRequest1)).willReturn(userEntity1);
        given(userRepository.findByLogin(anyString())).willReturn(userEntity1);

        InvalidInputException exception = assertThrows(InvalidInputException.class,
                () -> userService.createUser(userRequest1));

        assertEquals("Employee login not unique",exception.getMessage());
        verify(userMapper, times(1)).mapUserRequestToEntity(any());
        verify(userRepository, times(1)).findById(anyString());
        verify(userRepository, times(1)).findByLogin(anyString());
        verify(userRepository, times(2));
        verify(userRepository, times(0)).save(any());
        verify(userMapper, times(0)).mapUserEntityToDto(any());
    }

    @Test
    void updateUser() {

        UserRequest userRequest1 = testUserRequest(1);
        UserEntity userEntity1 = testUserEntity(1);
        UserEntity userEntity1Update = testUserEntity(11);
        userEntity1Update.setId(userEntity1.getId());

        UserDto expectedUserDto = testUserDto(11);
        expectedUserDto.setId(userEntity1.getId());

        given(userRepository.findById(userEntity1.getId())).willReturn(userEntity1);
        given(userMapper.mapUserRequestToEntity(any())).willReturn(userEntity1Update);
        given(userRepository.findByLogin(userEntity1Update.getLogin())).willReturn(null);
        given(userRepository.save(userEntity1Update)).willReturn(userEntity1Update);
        given(userMapper.mapUserEntityToDto(userEntity1Update)).willReturn(expectedUserDto);

        UserDto userDto = userService.updateUser(userRequest1);

        verify(userRepository,times(1)).findById(userEntity1.getId());
        verify(userMapper, times(1)).mapUserRequestToEntity(any());
        verify(userRepository,times(1)).findByLogin(userEntity1Update.getLogin());
        verify(userRepository, times(1)).save(any());
        verify(userMapper, times(1)).mapUserEntityToDto(any());

        assertEquals(expectedUserDto, userDto);

    }

    @Test
    void updateUserDuplicateLogin() {

        UserRequest userRequest1 = testUserRequest(1);
        UserEntity userEntity1 = testUserEntity(1);
        UserEntity userEntity1Update = testUserEntity(11);
        userEntity1Update.setId(userEntity1.getId());

        given(userRepository.findById(userEntity1.getId())).willReturn(userEntity1);
        given(userMapper.mapUserRequestToEntity(any())).willReturn(userEntity1Update);
        given(userRepository.findByLogin(userEntity1Update.getLogin())).willReturn(userEntity1Update);

        InvalidInputException exception = assertThrows(InvalidInputException.class,
                () -> userService.updateUser(userRequest1));

        assertEquals("Employee login not unique", exception.getMessage());

        verify(userRepository,times(1)).findById(userEntity1.getId());
        verify(userMapper, times(1)).mapUserRequestToEntity(any());
        verify(userRepository,times(1)).findByLogin(userEntity1Update.getLogin());
        verify(userRepository, times(0)).save(any());
        verify(userMapper, times(0)).mapUserEntityToDto(any());

    }

    @Test
    void updateUserNotExists() {


        UserRequest userRequest1 = testUserRequest(1);
        UserEntity userEntity1 = testUserEntity(1);

        UserDto expectedUserDto = testUserDto(11);
        expectedUserDto.setId(userEntity1.getId());

        given(userRepository.findById(userEntity1.getId())).willReturn(null);

        InvalidInputException exception = assertThrows(InvalidInputException.class,
                () -> userService.updateUser(userRequest1));

        assertEquals("No such employee", exception.getMessage());

        verify(userRepository,times(1)).findById(userEntity1.getId());
        verify(userMapper, times(0)).mapUserRequestToEntity(any());
        verify(userRepository, times(0)).save(any());
        verify(userMapper, times(0)).mapUserEntityToDto(any());
    }

    @Test
    void deleteUser() {

        UserRequest userRequest1 = testUserRequest(1);
        UserEntity userEntity1 = testUserEntity(1);

        UserDto expectedUserDto = testUserDto(11);
        expectedUserDto.setId(userEntity1.getId());

        given(userRepository.findById(userEntity1.getId())).willReturn(userEntity1);

        userService.deleteUser(userRequest1.getId());

        verify(userRepository,times(1)).findById(userEntity1.getId());
        verify(userMapper, times(1)).mapUserRequestToEntity(any());
        verify(userRepository,times(1)).deleteById(userEntity1.getId());

    }

    @Test
    void deleteUserNotExists() {

        UserRequest userRequest1 = testUserRequest(1);
        UserEntity userEntity1 = testUserEntity(1);

        UserDto expectedUserDto = testUserDto(11);
        expectedUserDto.setId(userEntity1.getId());

        given(userRepository.findById(userEntity1.getId())).willReturn(null);

        InvalidInputException exception =
                assertThrows(InvalidInputException.class,()->userService.deleteUser(userRequest1.getId()));

        assertEquals("No such employee",exception.getMessage());
        verify(userRepository,times(1)).findById(userEntity1.getId());
        verify(userMapper, times(1)).mapUserRequestToEntity(any());
        verify(userRepository,times(1)).deleteById(userEntity1.getId());

    }

    @AfterEach
    void tearDown() {

    }

    UserRequest testUserRequest(int index) {
        return new UserRequest().builder()
                .id("id" + index)
                .login("login" + index)
                .name("name" + index)
                .salary(new BigDecimal(index + ".12"))
                .startDate("2020-01-01")
                .build();
    }

    UserDto testUserDto(int index) {
        return new UserDto().builder()
                .id("id" + index)
                .login("login" + index)
                .name("name" + index)
                .salary(new BigDecimal(index + ".12"))
                .startDate("2020-01-01")
                .build();
    }

    UserEntity testUserEntity(int index) {
        UserEntity userEntity = new UserEntity();
        userEntity.setId("id" + index);
        userEntity.setLogin("login" + index);
        userEntity.setName("name" + index);
        userEntity.setSalary(new BigDecimal(index + ".12"));
        try {
            userEntity.setStartDate(new SimpleDateFormat("yyyy-MM-dd").parse("2020-01-01"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return userEntity;
    }


}