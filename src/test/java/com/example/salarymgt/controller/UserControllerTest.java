package com.example.salarymgt.controller;

import com.example.salarymgt.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @MockBean
    UserService userService;

    @Test
    void uploadUser() {
        //todo
    }

    @Test
    void uploadUserNoData() {
        //todo
    }

    @Test
    void uploadUserInvalidInput() {
        //todo
    }

    @Test
    void fetchUsers() {
        //todo
    }

    @Test
    void fetchUsersInvalidInput() {
        //todo
    }

    @Test
    void getUser() {
        //todo
    }

    @Test
    void getUserNotExists() {
        //todo
    }

    @Test
    void createUser() {
        //todo
    }

    @Test
    void createUserDuplicateID() {
        //todo
    }

    @Test
    void createUserDuplicateLogin() {
        //todo
    }

    @Test
    void createUserInvalidSalary() {
        //todo
    }

    @Test
    void createUserInvalidDate() {
        //todo
    }

    @Test
    void updateUser() {
        //todo
    }

    @Test
    void updateUserNotExists() {
        //todo
    }

    @Test
    void updateUserDuplicateLogin() {
        //todo
    }

    @Test
    void updateUserInvalidSalary() {
        //todo
    }

    @Test
    void updateUserInvalidDate() {
        //todo
    }

    @Test
    void deleteUser() {
        //todo
    }

    @Test
    void deleteUserNotExists() {
        //todo
    }

    @Test
    void handleException() {
        //todo
    }

}