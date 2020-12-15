package com.example.salarymgt.controller;

import com.example.salarymgt.TestUtil;
import com.example.salarymgt.dto.UserDto;
import com.example.salarymgt.exception.InvalidInputException;
import com.example.salarymgt.mapper.UserMapper;
import com.example.salarymgt.request.UserRequest;
import com.example.salarymgt.response.MessageResponse;
import com.example.salarymgt.response.UserResponse;
import com.example.salarymgt.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.io.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @MockBean
    UserService userService;

    @MockBean
    UserMapper userMapper;

    @Autowired
    MockMvc mockMvc;

    @Test
    void uploadUser() throws Exception {
        String csv = readFile("src/test/resources/testData.csv");

        MvcResult result = mockMvc.perform(post("/users/upload")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .content(csv)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(201))
                .andReturn();

        MessageResponse messageResponse = new ObjectMapper().readValue(result.getResponse().getContentAsString(),
                MessageResponse.class);
        assertEquals("Data created or updated", messageResponse.getMessage());
        verify(userService, times(1)).uploadUsers(any());
    }

    @Test
    void uploadUserEmpty() throws Exception {
        String csv = readFile("src/test/resources/testDataEmpty.csv");
        MvcResult result = mockMvc.perform(post("/users/upload")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .content(csv)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200))
                .andReturn();

        MessageResponse messageResponse = new ObjectMapper().readValue(result.getResponse().getContentAsString(),
                MessageResponse.class);
        assertEquals("Success but no data updated", messageResponse.getMessage());
        verify(userService, times(0)).uploadUsers(any());
    }

    @Test
    void uploadUserInvalidDate() throws Exception {
        String csv = readFile("src/test/resources/testDataInvalidDate.csv");
        MvcResult result = mockMvc.perform(post("/users/upload")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .content(csv)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(400))
                .andReturn();

        MessageResponse messageResponse = new ObjectMapper().readValue(result.getResponse().getContentAsString(),
                MessageResponse.class);
        assertEquals("Bad input - parsing error, duplicate row, invalid salary etc.", messageResponse.getMessage());
        verify(userService, times(0)).uploadUsers(any());
    }

    @Test
    void uploadUserInvalidSalary() throws Exception {
        String csv = readFile("src/test/resources/testDataInvalidSalary.csv");
        MvcResult result = mockMvc.perform(post("/users/upload")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .content(csv)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(400))
                .andReturn();

        MessageResponse messageResponse = new ObjectMapper().readValue(result.getResponse().getContentAsString(),
                MessageResponse.class);
        assertEquals("Bad input - parsing error, duplicate row, invalid salary etc.", messageResponse.getMessage());
        verify(userService, times(0)).uploadUsers(any());
    }

    @Test
    void uploadUserDuplicateRow() throws Exception {
        String csv = readFile("src/test/resources/testDataDuplicateRow.csv");
        MvcResult result = mockMvc.perform(post("/users/upload")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .content(csv)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(400))
                .andReturn();

        MessageResponse messageResponse = new ObjectMapper().readValue(result.getResponse().getContentAsString(),
                MessageResponse.class);
        assertEquals("Bad input - parsing error, duplicate row, invalid salary etc.", messageResponse.getMessage());
        verify(userService, times(0)).uploadUsers(any());
    }

    @Test
    void fetchUsers() throws Exception {

        List<UserDto> userDtos = new ArrayList<>();
        UserDto userDto1 = TestUtil.testUserDto(1);
        UserDto userDto2 = TestUtil.testUserDto(2);
        UserDto userDto3 = TestUtil.testUserDto(3);
        userDtos.add(userDto1);
        userDtos.add(userDto2);
        userDtos.add(userDto3);

        List<UserResponse> userResponses = new ArrayList<>();
        UserResponse userResponse1 = TestUtil.testUserResponse(1);
        UserResponse userResponse2 = TestUtil.testUserResponse(2);
        UserResponse userResponse3 = TestUtil.testUserResponse(3);
        userResponses.add(userResponse1);
        userResponses.add(userResponse2);
        userResponses.add(userResponse3);

        given(userService.fetchUsers(new BigDecimal(1234), new BigDecimal(4567.0), 15, 100)).willReturn(userDtos);
        given(userMapper.mapUserDtoToUserResponse(userDto1)).willReturn(userResponse1);
        given(userMapper.mapUserDtoToUserResponse(userDto2)).willReturn(userResponse2);
        given(userMapper.mapUserDtoToUserResponse(userDto3)).willReturn(userResponse3);

        MvcResult result =
                mockMvc.perform(get("/users?minSalary=1&maxSalary=4567.0&offset=1&limit=5"))
                        .andExpect(status().is(200)).andReturn();


        List<UserResponse> responses =
                new ObjectMapper().readValue(result.getResponse().getContentAsString(),
                        new TypeReference<List<UserResponse>>() {
                        });

        assertEquals(responses, userResponses);

        verify(userService, times(1)).fetchUsers(new BigDecimal(1234), new BigDecimal(4567.0), 15, 100);
        verify(userMapper, times(1)).mapUserDtoToUserResponse(userDto1);
        verify(userMapper, times(1)).mapUserDtoToUserResponse(userDto2);
        verify(userMapper, times(1)).mapUserDtoToUserResponse(userDto3);
    }

    @Test
    void fetchUsersNoParam() throws Exception {

        List<UserDto> userDtos = new ArrayList<>();
        UserDto userDto1 = TestUtil.testUserDto(1);
        UserDto userDto2 = TestUtil.testUserDto(2);
        UserDto userDto3 = TestUtil.testUserDto(3);
        userDtos.add(userDto1);
        userDtos.add(userDto2);
        userDtos.add(userDto3);

        List<UserResponse> userResponses = new ArrayList<>();
        UserResponse userResponse1 = TestUtil.testUserResponse(1);
        UserResponse userResponse2 = TestUtil.testUserResponse(2);
        UserResponse userResponse3 = TestUtil.testUserResponse(3);
        userResponses.add(userResponse1);
        userResponses.add(userResponse2);
        userResponses.add(userResponse3);

        given(userService.fetchUsers(null, null, null, null)).willReturn(userDtos);
        given(userMapper.mapUserDtoToUserResponse(userDto1)).willReturn(userResponse1);
        given(userMapper.mapUserDtoToUserResponse(userDto2)).willReturn(userResponse2);
        given(userMapper.mapUserDtoToUserResponse(userDto3)).willReturn(userResponse3);

        MvcResult result =
                mockMvc.perform(get("/users"))
                        .andExpect(status().is(200)).andReturn();


        List<UserResponse> responses =
                new ObjectMapper().readValue(result.getResponse().getContentAsString(),
                        new TypeReference<List<UserResponse>>() {
                        });

        assertEquals(responses, userResponses);

        verify(userService, times(1)).fetchUsers(null, null, null, null);
        verify(userMapper, times(1)).mapUserDtoToUserResponse(userDto1);
        verify(userMapper, times(1)).mapUserDtoToUserResponse(userDto2);
        verify(userMapper, times(1)).mapUserDtoToUserResponse(userDto3);
    }

    @Test
    void fetchUsersInvalidMinSalary() throws Exception {

        mockMvc.perform(get("/users?minSalary=invalid&maxSalary=4567.0&offset=1&limit=5"))
                .andExpect(status().is(400)).andReturn();

        verify(userService, times(0)).fetchUsers(any(), any(), any(), any());
        verify(userMapper, times(0)).mapUserDtoToUserResponse(any());
    }

    @Test
    void fetchUsersInvalidMaxSalary() throws Exception {

        mockMvc.perform(get("/users?minSalary=1234&maxSalary=invalid&offset=1&limit=5"))
                .andExpect(status().is(400)).andReturn();

        verify(userService, times(0)).fetchUsers(any(), any(), any(), any());
        verify(userMapper, times(0)).mapUserDtoToUserResponse(any());
    }

    @Test
    void fetchUsersInvalidOffset() throws Exception {

        mockMvc.perform(get("/users?minSalary=1234&maxSalary=4567.0&offset=invalid&limit=5"))
                .andExpect(status().is(400)).andReturn();

        verify(userService, times(0)).fetchUsers(any(), any(), any(), any());
        verify(userMapper, times(0)).mapUserDtoToUserResponse(any());
    }

    @Test
    void fetchUsersInvalidLimit() throws Exception {

        mockMvc.perform(get("/users?minSalary=1234&maxSalary=4567.0&offset=1&limit=invalid"))
                .andExpect(status().is(400)).andReturn();

        verify(userService, times(0)).fetchUsers(any(), any(), any(), any());
        verify(userMapper, times(0)).mapUserDtoToUserResponse(any());
    }

    @Test
    void getUser() throws Exception {
        UserDto userDto = TestUtil.testUserDto(123);
        UserResponse userResponse = TestUtil.testUserResponse(123);
        given(userService.getUser("123")).willReturn(userDto);
        given(userMapper.mapUserDtoToUserResponse(userDto)).willReturn(userResponse);

        MvcResult mvcResult = mockMvc.perform(get("/users/123")).andExpect(status().is(200)).andReturn();

        UserResponse response = new ObjectMapper().readValue(mvcResult.getResponse().getContentAsString(),
                UserResponse.class);
        assertEquals(userResponse, response);

        verify(userService, times(1)).getUser("123");
        verify(userMapper, times(1)).mapUserDtoToUserResponse(userDto);
    }

    @Test
    void getUserNotExists() throws Exception {
        given(userService.getUser("123")).willReturn(null);
        mockMvc.perform(get("/users/123")).andExpect(status().is(400)).andReturn();

        verify(userService, times(1)).getUser("123");
        verify(userMapper, times(0)).mapUserDtoToUserResponse(any());
    }

    @Test
    void createUser() throws Exception {

        UserRequest userRequest = TestUtil.testUserRequest(1);
        UserDto userDto = TestUtil.testUserDto(1);
        MessageResponse messageResponse = new MessageResponse().builder().message("Successfully created").build();

        given(userService.createUser(userRequest)).willReturn(userDto);

        MvcResult mvcResult =
                mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(readFile("src/test/resources/userValid.json"))
                        .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().is(201))
                        .andReturn();

        MessageResponse response = new ObjectMapper().readValue(mvcResult.getResponse().getContentAsString(),
                MessageResponse.class);

        assertEquals(messageResponse,response);
        verify(userService,times(1)).createUser(userRequest);
    }

    @Test
    void createUserDuplicateID() throws Exception {

        UserRequest userRequest = TestUtil.testUserRequest(1);

        given(userService.createUser(userRequest)).willThrow(new InvalidInputException("Employee ID already exists"));

        MvcResult mvcResult =
                mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(readFile("src/test/resources/userValid.json"))
                        .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().is(400))
                        .andReturn();

        MessageResponse response = new ObjectMapper()
                .readValue(mvcResult.getResponse().getContentAsString(),MessageResponse.class);

        assertEquals("Employee ID already exists",response.getMessage());
        verify(userService,times(0)).createUser(any());
    }

    @Test
    void createUserDuplicateLogin() throws Exception {
        UserRequest userRequest = TestUtil.testUserRequest(1);

        given(userService.createUser(userRequest)).willThrow(new InvalidInputException("Employee login not unique"));

        MvcResult mvcResult =
                mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON).content(readFile("src/test/resources/userValid.json"))
                        .accept(MediaType.APPLICATION_JSON)).andExpect(status().is(400)).andReturn();

        MessageResponse response = new ObjectMapper()
                .readValue(mvcResult.getResponse().getContentAsString(), MessageResponse.class);

        assertEquals("Employee login not unique",response.getMessage());
        verify(userService,times(0)).createUser(any());
    }

    @Test
    void createUserInvalidSalary() throws Exception {

        MvcResult mvcResult =
                mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(readFile("src/test/resources/userInvalidSalary.json"))
                        .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().is(400))
                        .andReturn();

        MessageResponse response = new ObjectMapper()
                .readValue(mvcResult.getResponse().getContentAsString(), MessageResponse.class);

        assertEquals("Invalid salary",response.getMessage());
        verify(userService,times(0)).createUser(any());
    }

    @Test
    void createUserInvalidDate() throws Exception {
        MvcResult mvcResult =
                mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(readFile("src/test/resources/userInvalidDate.json"))
                        .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().is(400))
                        .andReturn();

        MessageResponse response = new ObjectMapper()
                .readValue(mvcResult.getResponse().getContentAsString(),MessageResponse.class);

        assertEquals("Invalid date",response.getMessage());
        verify(userService,times(0)).createUser(any());
    }

    @Test
    void updateUser() throws Exception {
        UserRequest userRequest = TestUtil.testUserRequest(1);
        UserDto userDto = TestUtil.testUserDto(1);

        given(userService.updateUser(userRequest)).willReturn(userDto);

        MvcResult mvcResult =
                mockMvc.perform(post("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(readFile("src/test/resources/userValid.json"))
                        .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().is(200))
                        .andReturn();

        MessageResponse response = new ObjectMapper()
                .readValue(mvcResult.getResponse().getContentAsString(),MessageResponse.class);

        assertEquals("Successfully updated",response.getMessage());
        verify(userService,times(1)).updateUser(userRequest);
    }

    @Test
    void updateUserNotExists() throws Exception {
        UserRequest userRequest = TestUtil.testUserRequest(1);

        given(userService.updateUser(userRequest)).willThrow(new InvalidInputException("No such employee"));

        MvcResult mvcResult =
                mockMvc.perform(post("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(readFile("src/test/resources/userValid.json"))
                        .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().is(200))
                        .andReturn();

        MessageResponse response = new ObjectMapper()
                .readValue(mvcResult.getResponse().getContentAsString(), MessageResponse.class);

        assertEquals("No such employee",response.getMessage());
        verify(userService,times(1)).updateUser(userRequest);
    }

    @Test
    void updateUserDuplicateLogin() throws Exception {
        UserRequest userRequest = TestUtil.testUserRequest(1);

        given(userService.updateUser(userRequest)).willThrow(new InvalidInputException("Employee login not unique"));

        MvcResult mvcResult =
                mockMvc.perform(post("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(readFile("src/test/resources/userValid.json"))
                        .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().is(200))
                        .andReturn();

        MessageResponse response = new ObjectMapper()
                .readValue(mvcResult.getResponse().getContentAsString(), MessageResponse.class);

        assertEquals("Employee login not unique",response.getMessage());
        verify(userService,times(1)).updateUser(userRequest);
    }

    @Test
    void updateUserInvalidSalary() throws Exception {

        MvcResult mvcResult =
                mockMvc.perform(post("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(readFile("src/test/resources/userInvalidSalary.json"))
                        .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().is(200))
                        .andReturn();

        MessageResponse response = new ObjectMapper().readValue(mvcResult.getResponse().getContentAsString(),
                MessageResponse.class);

        assertEquals("Invalid salary",response.getMessage());
        verify(userService,times(0)).updateUser(any());
    }

    @Test
    void updateUserInvalidDate() throws Exception {
        MvcResult mvcResult =
                mockMvc.perform(post("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(readFile("src/test/resources/userInvalidDate.json"))
                        .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().is(200))
                        .andReturn();

        MessageResponse response = new ObjectMapper().readValue(mvcResult.getResponse().getContentAsString(),
                MessageResponse.class);

        assertEquals("Invalid date",response.getMessage());
        verify(userService,times(0)).updateUser(any());
    }

    @Test
    void deleteUser() throws Exception {
        UserDto userDto = mock(UserDto.class);
        given(userService.getUser("1")).willReturn(userDto);
        MvcResult mvcResult = mockMvc.perform(delete("/user/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200)).andReturn();
        MessageResponse response = new ObjectMapper().readValue(mvcResult.getResponse().getContentAsString(),
                MessageResponse.class);
        assertEquals("Successfully deleted",response.getMessage());
    }

    @Test
    void deleteUserNotExists() throws Exception {
        UserDto userDto = mock(UserDto.class);
        given(userService.getUser("1")).willReturn(null);
        MvcResult mvcResult = mockMvc.perform(delete("/user/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(400)).andReturn();
        MessageResponse response = new ObjectMapper().readValue(mvcResult.getResponse().getContentAsString(),
                MessageResponse.class);
        assertEquals("No such employee",response.getMessage());
    }

    @Test
    void handleException() {
        //todo
    }

    public String readFile(String path) {
        StringBuilder csv = new StringBuilder();
        String line;

        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            while ((line = reader.readLine()) != null) {
                csv.append(line);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("Invalid path " + path);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error while reading file " + path);
        }
        return csv.toString();
    }

}