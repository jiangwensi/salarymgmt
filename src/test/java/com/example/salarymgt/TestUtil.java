package com.example.salarymgt;

import com.example.salarymgt.dto.UserDto;
import com.example.salarymgt.entity.UserEntity;
import com.example.salarymgt.request.UserRequest;
import com.example.salarymgt.response.UserResponse;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by Jiang Wensi on 15/12/2020
 */
public class TestUtil {

    public static UserRequest testUserRequest(int index) {
        return new UserRequest().builder()
                .id("id" + index)
                .login("login" + index)
                .name("name" + index)
                .salary(new BigDecimal(index + ".12"))
                .startDate("2020-01-01")
                .build();
    }

    public static UserResponse testUserResponse(int index){
        return new UserResponse().builder()
                .id("id" + index)
                .login("login" + index)
                .name("name" + index)
                .salary(new BigDecimal(index+".12"))
                .startDate("2020-01-01")
                .build();
    }

    public static UserDto testUserDto(int index) {
        return new UserDto().builder()
                .id("id" + index)
                .login("login" + index)
                .name("name" + index)
                .salary(new BigDecimal(index + ".12"))
                .startDate("2020-01-01")
                .build();
    }

    public static UserEntity testUserEntity(int index) {
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
