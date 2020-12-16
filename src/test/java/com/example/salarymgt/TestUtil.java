package com.example.salarymgt;

import com.example.salarymgt.dto.UserDto;
import com.example.salarymgt.entity.UserEntity;
import com.example.salarymgt.request.UserRequest;
import com.example.salarymgt.response.UserResponse;
import com.example.salarymgt.util.NumUtil;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by Jiang Wensi on 15/12/2020
 */
public class TestUtil {

    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    public static UserRequest testUserRequest(int index) {
        try {
            return new UserRequest().builder()
                    .id("id" + index)
                    .login("login" + index)
                    .name("name" + index)
                    .salary(NumUtil.bigDecimal(index + ".12"))
                    .startDate(sdf.parse("2020-01-01"))
                    .build();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static UserResponse testUserResponse(int index) {
        return new UserResponse().builder()
                .id("id" + index)
                .login("login" + index)
                .name("name" + index)
                .salary(NumUtil.bigDecimal(index + ".12"))
                .startDate("2020-01-01")
                .build();
    }

    public static UserDto testUserDto(int index) {
        try {
            return new UserDto().builder()
                    .id("id" + index)
                    .login("login" + index)
                    .name("name" + index)
                    .salary(NumUtil.bigDecimal(index + ".12"))
                    .startDate(sdf.parse("2020-01-01"))
                    .build();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static UserEntity testUserEntity(int index) {
        UserEntity userEntity = new UserEntity();
        userEntity.setId("id" + index);
        userEntity.setLogin("login" + index);
        userEntity.setName("name" + index);
        userEntity.setSalary(NumUtil.bigDecimal(index + ".12"));
        try {
            userEntity.setStartDate(sdf.parse("2020-01-01"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return userEntity;
    }
}
