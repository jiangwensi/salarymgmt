package com.example.salarymgt.controller;

import com.example.salarymgt.TestUtil;
import com.example.salarymgt.dto.UserDto;
import com.example.salarymgt.exception.InvalidInputException;
import com.example.salarymgt.mapper.UserMapper;
import com.example.salarymgt.request.UserRequest;
import com.example.salarymgt.response.FetchUsersResponse;
import com.example.salarymgt.response.MessageResponse;
import com.example.salarymgt.response.UserResponse;
import com.example.salarymgt.service.UserService;
import com.example.salarymgt.util.DateUtil;
import com.example.salarymgt.util.NumUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
    void uploadUsers() throws Exception {
        String csv = readFile("src/test/resources/testData.csv");
        MockMultipartFile csvFile = new MockMultipartFile("file", "testData.csv", "text/csv", csv.getBytes());

        List<UserRequest> userRequests = new ArrayList<>();
        userRequests.add(new UserRequest().builder().id("e0001").login("hpotter").name("Harry Potter").salary(NumUtil.bigDecimal(1234.00)).startDate(DateUtil.parse("16-Nov-01")).build());
        userRequests.add(new UserRequest().builder().id("e0002").login("rwesley").name("Ron Weasley").salary(NumUtil.bigDecimal(19234.50)).startDate(DateUtil.parse("2001-11-16")).build());
        userRequests.add(new UserRequest().builder().id("e0003").login("ssnape").name("Severus Snape").salary(NumUtil.bigDecimal(4000.0)).startDate(DateUtil.parse("2001-11-16")).build());
        userRequests.add(new UserRequest().builder().id("e0004").login("rhagrid").name("Rubeus Hagrid").salary(NumUtil.bigDecimal(3999.999)).startDate(DateUtil.parse("16-Nov-01")).build());
        userRequests.add(new UserRequest().builder().id("e0005").login("voldemort").name("Lord Voldemort").salary(NumUtil.bigDecimal(523.4)).startDate(DateUtil.parse("17-Nov-01")).build());
        userRequests.add(new UserRequest().builder().id("e0006").login("gwesley").name("Ginny Weasley").salary(NumUtil.bigDecimal(4000.004)).startDate(DateUtil.parse("18-Nov-01")).build());
        userRequests.add(new UserRequest().builder().id("e0007").login("hgranger").name("Hermione Granger").salary(NumUtil.bigDecimal(0.0)).startDate(DateUtil.parse("2001-11-18")).build());
        userRequests.add(new UserRequest().builder().id("e0008").login("adumbledore").name("Albus Dumbledore").salary(NumUtil.bigDecimal(34.23)).startDate(DateUtil.parse("2001-11-19")).build());
        userRequests.add(new UserRequest().builder().id("e0009").login("dmalfoy").name("Draco Malfoy").salary(NumUtil.bigDecimal(34234.5)).startDate(DateUtil.parse("2001-11-20")).build());
        userRequests.add(new UserRequest().builder().id("e0010").login("basilisk").name("Basilisk").salary(NumUtil.bigDecimal(23.43)).startDate(DateUtil.parse("21-Nov-01")).build());

        List<UserDto> userDtos = new ArrayList<>();
        userDtos.add(mock(UserDto.class));

        given(userService.uploadUsers(userRequests)).willReturn(userDtos);
        MvcResult result = mockMvc.perform(multipart("/users/upload")
                .file(csvFile)
                .characterEncoding("UTF-8")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(201))
                .andReturn();

        MessageResponse messageResponse = new ObjectMapper().readValue(result.getResponse().getContentAsString(),
                MessageResponse.class);
        assertEquals("Data created or updated", messageResponse.getMessage());
        verify(userService, times(1)).uploadUsers(any());
    }

    @Test
    void uploadUsersEmpty() throws Exception {
        String csv = readFile("src/test/resources/testDataEmpty.csv");
        MockMultipartFile csvFile = new MockMultipartFile("file", "testData.csv", "text/csv", csv.getBytes());
        MvcResult result = mockMvc.perform(multipart("/users/upload")
                .file(csvFile)
                .characterEncoding("UTF-8")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200))
                .andReturn();

        MessageResponse messageResponse = new ObjectMapper().readValue(result.getResponse().getContentAsString(),
                MessageResponse.class);
        assertEquals("Success but no data updated", messageResponse.getMessage());
        verify(userService, times(0)).uploadUsers(any());
    }

    @Test
    void uploadUsersInvalidDate() throws Exception {
        String csv = readFile("src/test/resources/testDataInvalidDate.csv");
        MockMultipartFile csvFile = new MockMultipartFile("file", "testDataInvalidDate.csv", "text/csv", csv.getBytes());
        MvcResult result = mockMvc.perform(multipart("/users/upload")
                .file(csvFile)
                .characterEncoding("UTF-8")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(400))
                .andReturn();

        MessageResponse messageResponse = new ObjectMapper().readValue(result.getResponse().getContentAsString(),
                MessageResponse.class);
        assertEquals("Parsing Error. Please check date format", messageResponse.getMessage());
        verify(userService, times(0)).uploadUsers(any());
    }

    @Test
    void uploadUsersInvalidSalary() throws Exception {
        String csv = readFile("src/test/resources/testDataInvalidSalary.csv");
        MockMultipartFile csvFile = new MockMultipartFile("file", "testDataInvalidSalary.csv", "text/csv", csv.getBytes());
        MvcResult result = mockMvc.perform(multipart("/users/upload")
                .file(csvFile)
                .characterEncoding("UTF-8")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(400))
                .andReturn();

        MessageResponse messageResponse = new ObjectMapper().readValue(result.getResponse().getContentAsString(),
                MessageResponse.class);
        assertEquals("Parsing Error. Please check salary format", messageResponse.getMessage());
        verify(userService, times(0)).uploadUsers(any());
    }

    @Test
    void uploadUsersDuplicateRow() throws Exception {
        String csv = readFile("src/test/resources/testDataDuplicateRow.csv");
        MockMultipartFile csvFile = new MockMultipartFile("file", "testDataDuplicateRow.csv", "text/csv", csv.getBytes());
        MvcResult result = mockMvc.perform(multipart("/users/upload")
                .file(csvFile)
                .characterEncoding("UTF-8")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(400))
                .andReturn();

        MessageResponse messageResponse = new ObjectMapper().readValue(result.getResponse().getContentAsString(),
                MessageResponse.class);
        assertEquals("Duplicate rows found in the file", messageResponse.getMessage());
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


        given(userService.fetchUsers(NumUtil.bigDecimal(1234), NumUtil.bigDecimal(4567.0), 15, 100)).willReturn(userDtos);
        given(userMapper.mapUserDtoToUserResponse(userDto1)).willReturn(userResponse1);
        given(userMapper.mapUserDtoToUserResponse(userDto2)).willReturn(userResponse2);
        given(userMapper.mapUserDtoToUserResponse(userDto3)).willReturn(userResponse3);

        MvcResult result =
                mockMvc.perform(get("/users?minSalary=1234&maxSalary=4567.0&offset=15&limit=100"))
                        .andExpect(status().is(200)).andReturn();

        FetchUsersResponse responses =
                new ObjectMapper().readValue(result.getResponse().getContentAsString(),
                        FetchUsersResponse.class);

        assertEquals(new FetchUsersResponse().builder().results(userResponses).build(), responses);

        verify(userService, times(1)).fetchUsers(NumUtil.bigDecimal(1234), NumUtil.bigDecimal(4567.0), 15, 100);
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



        FetchUsersResponse responses =
                new ObjectMapper().readValue(result.getResponse().getContentAsString(),
                        FetchUsersResponse.class);

        assertEquals(new FetchUsersResponse().builder().results(userResponses).build(), responses);

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

        String content = readFile("src/test/resources/userValid.json");
        MvcResult mvcResult =
                mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                        .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().is(201))
                        .andReturn();

        MessageResponse response = new ObjectMapper().readValue(mvcResult.getResponse().getContentAsString(),
                MessageResponse.class);

        assertEquals(messageResponse, response);
        verify(userService, times(1)).createUser(any());
    }

    @Test
    void createUserDuplicateID() throws Exception {

        given(userService.createUser(any())).willThrow(new InvalidInputException("Employee ID already exists"));

        MvcResult mvcResult =
                mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(readFile("src/test/resources/userValid.json"))
                        .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().is(400))
                        .andReturn();

        MessageResponse response = new ObjectMapper()
                .readValue(mvcResult.getResponse().getContentAsString(), MessageResponse.class);

        assertEquals("Employee ID already exists", response.getMessage());
        verify(userService, times(1)).createUser(any());
    }

    @Test
    void createUserDuplicateLogin() throws Exception {

        given(userService.createUser(any())).willThrow(new InvalidInputException("Employee login not unique"));

        MvcResult mvcResult =
                mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON).content(readFile("src/test/resources/userValid.json"))
                        .accept(MediaType.APPLICATION_JSON)).andExpect(status().is(400)).andReturn();

        MessageResponse response = new ObjectMapper()
                .readValue(mvcResult.getResponse().getContentAsString(), MessageResponse.class);

        assertEquals("Employee login not unique", response.getMessage());
        verify(userService, times(1)).createUser(any());
    }

    @Test
    void createUserInvalidSalary() throws Exception {

        String content = readFile("src/test/resources/userInvalidSalary.json");
        MvcResult mvcResult =
                mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                        .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().is(400))
                        .andReturn();

        System.out.println(mvcResult);
        MessageResponse response = new ObjectMapper()
                .readValue(mvcResult.getResponse().getContentAsString(), MessageResponse.class);

        assertEquals("Invalid salary", response.getMessage());
        verify(userService, times(0)).createUser(any());
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
                .readValue(mvcResult.getResponse().getContentAsString(), MessageResponse.class);

        assertEquals("Invalid date", response.getMessage());
        verify(userService, times(0)).createUser(any());
    }

    @Test
    void updateUser() throws Exception {
        UserDto userDto = TestUtil.testUserDto(1);

        given(userService.updateUser(any())).willReturn(userDto);

        MvcResult mvcResult =
                mockMvc.perform(patch("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(readFile("src/test/resources/userValid.json"))
                        .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().is(200))
                        .andReturn();

        MessageResponse response = new ObjectMapper()
                .readValue(mvcResult.getResponse().getContentAsString(), MessageResponse.class);

        assertEquals("Successfully updated", response.getMessage());
        verify(userService, times(1)).updateUser(any());
    }

    @Test
    void updateUserNotExists() throws Exception {

        given(userService.updateUser(any())).willThrow(new InvalidInputException("No such employee"));

        MvcResult mvcResult =
                mockMvc.perform(patch("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(readFile("src/test/resources/userValid.json"))
                        .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().is(400))
                        .andReturn();

        MessageResponse response = new ObjectMapper()
                .readValue(mvcResult.getResponse().getContentAsString(), MessageResponse.class);

        assertEquals("No such employee", response.getMessage());
        verify(userService, times(1)).updateUser(any());
    }

    @Test
    void updateUserDuplicateLogin() throws Exception {

        given(userService.updateUser(any())).willThrow(new InvalidInputException("Employee login not unique"));

        MvcResult mvcResult =
                mockMvc.perform(patch("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(readFile("src/test/resources/userValid.json"))
                        .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().is(400))
                        .andReturn();

        MessageResponse response = new ObjectMapper()
                .readValue(mvcResult.getResponse().getContentAsString(), MessageResponse.class);

        assertEquals("Employee login not unique", response.getMessage());
        verify(userService, times(1)).updateUser(any());
    }

    @Test
    void updateUserInvalidSalary() throws Exception {

        MvcResult mvcResult =
                mockMvc.perform(patch("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(readFile("src/test/resources/userInvalidSalary.json"))
                        .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().is(400))
                        .andReturn();

        MessageResponse response = new ObjectMapper().readValue(mvcResult.getResponse().getContentAsString(),
                MessageResponse.class);

        assertEquals("Invalid salary", response.getMessage());
        verify(userService, times(0)).updateUser(any());
    }

    @Test
    void updateUserInvalidDate() throws Exception {
        MvcResult mvcResult =
                mockMvc.perform(patch("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(readFile("src/test/resources/userInvalidDate.json"))
                        .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().is(400))
                        .andReturn();

        MessageResponse response = new ObjectMapper().readValue(mvcResult.getResponse().getContentAsString(),
                MessageResponse.class);

        assertEquals("Invalid date", response.getMessage());
        verify(userService, times(0)).updateUser(any());
    }

    @Test
    void deleteUser() throws Exception {
        UserDto userDto = mock(UserDto.class);
        given(userService.getUser("1")).willReturn(userDto);
        MvcResult mvcResult =
                mockMvc.perform(delete("/users/1")
                        .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().is(200))
                        .andReturn();
        MessageResponse response = new ObjectMapper().readValue(mvcResult.getResponse().getContentAsString(),
                MessageResponse.class);
        assertEquals("Successfully deleted", response.getMessage());
    }

    @Test
    void deleteUserNotExists() throws Exception {
        willThrow(new InvalidInputException("No such employee")).given(userService).deleteUser("1");
        MvcResult mvcResult = mockMvc.perform(delete("/users/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(400)).andReturn();
        MessageResponse response = new ObjectMapper().readValue(mvcResult.getResponse().getContentAsString(),
                MessageResponse.class);
        assertEquals("No such employee", response.getMessage());
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
                csv.append("\r\n");
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