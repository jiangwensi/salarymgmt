package com.example.salarymgt.service;

import com.example.salarymgt.TestUtil;
import com.example.salarymgt.dto.UserDto;
import com.example.salarymgt.entity.UserEntity;
import com.example.salarymgt.exception.InvalidInputException;
import com.example.salarymgt.mapper.UserMapper;
import com.example.salarymgt.repo.UserRepository;
import com.example.salarymgt.request.UserRequest;
import com.example.salarymgt.util.NumUtil;
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
        userRequests.add(TestUtil.testUserRequest(1));
        userRequests.add(TestUtil.testUserRequest(2));
        userRequests.add(TestUtil.testUserRequest(3));

        List<UserEntity> userEntities = new ArrayList<>();
        userEntities.add(TestUtil.testUserEntity(1));
        userEntities.add(TestUtil.testUserEntity(2));
        userEntities.add(TestUtil.testUserEntity(3));

        List<UserDto> expectedDtos = new ArrayList<>();
        expectedDtos.add(TestUtil.testUserDto(1));
        expectedDtos.add(TestUtil.testUserDto(2));
        expectedDtos.add(TestUtil.testUserDto(3));

        given(userRepository.saveAll(any())).willReturn(userEntities);

        given(userMapper.mapUserRequestToEntity(TestUtil.testUserRequest(1))).willReturn(TestUtil.testUserEntity(1));
        given(userMapper.mapUserRequestToEntity(TestUtil.testUserRequest(2))).willReturn(TestUtil.testUserEntity(2));
        given(userMapper.mapUserRequestToEntity(TestUtil.testUserRequest(3))).willReturn(TestUtil.testUserEntity(3));

        given(userMapper.mapUserEntityToDto(TestUtil.testUserEntity(1))).willReturn(TestUtil.testUserDto(1));
        given(userMapper.mapUserEntityToDto(TestUtil.testUserEntity(2))).willReturn(TestUtil.testUserDto(2));
        given(userMapper.mapUserEntityToDto(TestUtil.testUserEntity(3))).willReturn(TestUtil.testUserDto(3));

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
        verify(userRepository, times(0)).saveAll(any());
        assertEquals(0, userDtos.size());
    }

    @Test
    void uploadUsersNull() {
        List<UserRequest> userRequests = null;
        List<UserDto> userDtos = userService.uploadUsers(userRequests);
        verify(userRepository, times(0)).saveAll(any());
        assertEquals(0, userDtos.size());
    }

    @Test
    void fetchUsersAllParam() {
        BigDecimal minSalary = NumUtil.bigDecimal(1000);
        BigDecimal maxSalary = NumUtil.bigDecimal(2000);
        Integer offset = 10;
        Integer limit = 3;

        List<UserEntity> userEntities = new ArrayList<>();
        userEntities.add(TestUtil.testUserEntity(1111));
        userEntities.add(TestUtil.testUserEntity(1222));
        userEntities.add(TestUtil.testUserEntity(1333));

        List<UserDto> expectedDtos = new ArrayList<>();
        expectedDtos.add(TestUtil.testUserDto(1111));
        expectedDtos.add(TestUtil.testUserDto(1222));
        expectedDtos.add(TestUtil.testUserDto(1333));

        given(userMapper.mapUserEntityToDto(TestUtil.testUserEntity(1111))).willReturn(TestUtil.testUserDto(1111));
        given(userMapper.mapUserEntityToDto(TestUtil.testUserEntity(1222))).willReturn(TestUtil.testUserDto(1222));
        given(userMapper.mapUserEntityToDto(TestUtil.testUserEntity(1333))).willReturn(TestUtil.testUserDto(1333));

        given(userRepository.findBySalaryRangeOffsetLimit(minSalary, maxSalary, offset, limit)).willReturn(userEntities);
        List<UserDto> userDtos = userService.fetchUsers(minSalary, maxSalary, offset, limit);
        verify(userRepository, times(1)).findBySalaryRangeOffsetLimit(minSalary, maxSalary, offset, limit);
        assertEquals(expectedDtos, userDtos);

    }

    @Test
    void fetchUsersNoParam() {
        BigDecimal minSalary = NumUtil.bigDecimal(0);
        BigDecimal maxSalary = NumUtil.bigDecimal(0);
        BigDecimal maxSalaryRectified = NumUtil.bigDecimal(4000);
        Integer offset = 0;

        List<UserEntity> userEntities = new ArrayList<>();
        userEntities.add(TestUtil.testUserEntity(1111));
        userEntities.add(TestUtil.testUserEntity(1222));
        userEntities.add(TestUtil.testUserEntity(1333));

        List<UserDto> expectedDtos = new ArrayList<>();
        expectedDtos.add(TestUtil.testUserDto(1111));
        expectedDtos.add(TestUtil.testUserDto(1222));
        expectedDtos.add(TestUtil.testUserDto(1333));

        given(userMapper.mapUserEntityToDto(TestUtil.testUserEntity(1111))).willReturn(TestUtil.testUserDto(1111));
        given(userMapper.mapUserEntityToDto(TestUtil.testUserEntity(1222))).willReturn(TestUtil.testUserDto(1222));
        given(userMapper.mapUserEntityToDto(TestUtil.testUserEntity(1333))).willReturn(TestUtil.testUserDto(1333));

        given(userRepository.findBySalaryRangeOffset(minSalary, maxSalaryRectified, offset)).willReturn(userEntities);
        List<UserDto> userDtos = userService.fetchUsers(null, null, null, null);
        verify(userRepository, times(1)).findBySalaryRangeOffset(minSalary, maxSalaryRectified, offset);
        assertEquals(expectedDtos, userDtos);
    }

    @Test
    void getUser() {
        UserEntity userEntity1 = TestUtil.testUserEntity(1);
        UserDto expectedUserDto = TestUtil.testUserDto(1);

        given(userRepository.findById(userEntity1.getId())).willReturn(userEntity1);
        given(userMapper.mapUserEntityToDto(userEntity1)).willReturn(expectedUserDto);

        UserDto userDto = userService.getUser(userEntity1.getId());

        verify(userRepository, times(1)).findById(anyString());
        verify(userMapper, times(1)).mapUserEntityToDto(any());
        assertEquals(expectedUserDto, userDto);
    }

    @Test
    void createUser() {
        UserRequest userRequest1 = TestUtil.testUserRequest(1);
        UserEntity userEntity1 = TestUtil.testUserEntity(1);
        UserDto expectedUserDto = TestUtil.testUserDto(1);

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
        UserRequest userRequest1 = TestUtil.testUserRequest(1);
        UserEntity userEntity1 = TestUtil.testUserEntity(1);

        given(userMapper.mapUserRequestToEntity(userRequest1)).willReturn(userEntity1);
        given(userRepository.findById(anyString())).willReturn(userEntity1);

        InvalidInputException exception = assertThrows(InvalidInputException.class,
                () -> userService.createUser(userRequest1));

        assertEquals("Employee ID already exists",exception.getMessage());
        verify(userMapper, times(0)).mapUserRequestToEntity(any());
        verify(userRepository, times(1)).findById(anyString());
        verify(userRepository, times(0)).save(any());
        verify(userMapper, times(0)).mapUserEntityToDto(any());
    }

    @Test
    void createUserDuplicateLogin() {
        UserRequest userRequest1 = TestUtil.testUserRequest(1);
        UserEntity userEntity1 = TestUtil.testUserEntity(1);

        given(userMapper.mapUserRequestToEntity(userRequest1)).willReturn(userEntity1);
        given(userRepository.findByLogin(anyString())).willReturn(userEntity1);

        InvalidInputException exception = assertThrows(InvalidInputException.class,
                () -> userService.createUser(userRequest1));

        assertEquals("Employee login not unique",exception.getMessage());
        verify(userMapper, times(0)).mapUserRequestToEntity(any());
        verify(userRepository, times(1)).findById(anyString());
        verify(userRepository, times(1)).findByLogin(anyString());
        verify(userRepository, times(0)).save(any());
        verify(userMapper, times(0)).mapUserEntityToDto(any());
    }

    @Test
    void updateUser() {

        String updatedLogin = "1update";
        UserRequest userRequest1 = TestUtil.testUserRequest(1);
        UserEntity userEntity1 = TestUtil.testUserEntity(1);
        userRequest1.setLogin(updatedLogin);
        UserEntity userEntity1Update = TestUtil.testUserEntity(1);
        userEntity1Update.setLogin(updatedLogin);
        UserDto expectedUserDto = TestUtil.testUserDto(1);
        expectedUserDto.setLogin(updatedLogin);

        given(userRepository.findById(userEntity1.getId())).willReturn(userEntity1);
        given(userMapper.mapUserRequestToEntity(userRequest1)).willReturn(userEntity1Update);
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

        String updatedLogin = "1update";
        UserRequest userRequest1 = TestUtil.testUserRequest(1);
        userRequest1.setLogin(updatedLogin);
        UserEntity userEntity1 = TestUtil.testUserEntity(1);
        UserEntity userEntity1Update = TestUtil.testUserEntity(1);
        userEntity1Update.setLogin(updatedLogin);

        given(userRepository.findById(userEntity1.getId())).willReturn(userEntity1);
        given(userMapper.mapUserRequestToEntity(any())).willReturn(userEntity1Update);
        given(userRepository.findByLogin(updatedLogin)).willReturn(userEntity1Update);

        InvalidInputException exception = assertThrows(InvalidInputException.class,
                () -> userService.updateUser(userRequest1));

        assertEquals("Employee login not unique", exception.getMessage());

        verify(userRepository,times(1)).findById(userEntity1.getId());
        verify(userMapper, times(0)).mapUserRequestToEntity(any());
        verify(userRepository,times(1)).findByLogin(userEntity1Update.getLogin());
        verify(userRepository, times(0)).save(any());
        verify(userMapper, times(0)).mapUserEntityToDto(any());

    }

    @Test
    void updateUserNotExists() {

        UserRequest userRequest1 = TestUtil.testUserRequest(1);
        UserEntity userEntity1 = TestUtil.testUserEntity(1);

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

        UserRequest userRequest1 = TestUtil.testUserRequest(1);
        UserEntity userEntity1 = TestUtil.testUserEntity(1);

        given(userRepository.findById(userEntity1.getId())).willReturn(userEntity1);

        userService.deleteUser(userRequest1.getId());

        verify(userRepository,times(1)).findById(userEntity1.getId());
        verify(userRepository,times(1)).deleteById(userEntity1.getId());
    }

    @Test
    void deleteUserNotExists() {

        UserRequest userRequest1 = TestUtil.testUserRequest(1);
        UserEntity userEntity1 = TestUtil.testUserEntity(1);

        given(userRepository.findById(userEntity1.getId())).willReturn(null);

        InvalidInputException exception =
                assertThrows(InvalidInputException.class,()->userService.deleteUser(userRequest1.getId()));

        assertEquals("No such employee",exception.getMessage());
        verify(userRepository,times(1)).findById(userEntity1.getId());
        verify(userRepository,times(0)).deleteById(anyString());

    }

    @AfterEach
    void tearDown() {

    }



}