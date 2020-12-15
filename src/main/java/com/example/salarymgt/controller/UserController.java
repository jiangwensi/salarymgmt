package com.example.salarymgt.controller;

import com.example.salarymgt.dto.UserDto;
import com.example.salarymgt.mapper.UserMapper;
import com.example.salarymgt.request.UserRequest;
import com.example.salarymgt.response.MessageResponse;
import com.example.salarymgt.response.UserResponse;
import com.example.salarymgt.service.UserService;
import com.example.salarymgt.util.DateUtil;
import com.example.salarymgt.util.NumUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by Jiang Wensi on 11/12/2020
 */
@RestController
@RequestMapping("users")
@AllArgsConstructor
@Slf4j
public class UserController {

    private UserService userService;
    private UserMapper userMapper;

    @PostMapping(value = "/upload")
    public ResponseEntity<MessageResponse> uploadUser(@RequestParam("file") MultipartFile file) throws IOException {

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String[] titles = reader.readLine().split(",");
            if (!(titles[0].equals("id")
                    && titles[1].equals("login")
                    && titles[2].equals("name")
                    && titles[3].equals("salary")
                    && titles[4].equals("startDate"))) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new MessageResponse().builder()
                                .message("Unable to read the file. Please check file format")
                                .build());
            }

            List<UserRequest> userRequests = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");
                BigDecimal salary = NumUtil.bigDecimal(values[3]);
                if (salary.compareTo(BigDecimal.ZERO) < 0) {
                    log.error("Salary must not be negative");
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body(new MessageResponse().builder()
                                    .message("Parsing Error. Please check salary format")
                                    .build());
                }
                userRequests.add(new UserRequest().builder()
                        .id(values[0])
                        .login(values[1])
                        .name(values[2])
                        .salary(salary)
                        .startDate(DateUtil.parse(values[4]))
                        .build());
            }

            if (userRequests.size() == 0) {
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new MessageResponse().builder()
                                .message("Success but no data updated")
                                .build());
            }

            Set<UserRequest> userRequestHashSet = new HashSet<>(userRequests);
            if (userRequestHashSet.size() != userRequests.size()) {

                log.error("Duplicate rows found in the file");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new MessageResponse().builder()
                                .message("Duplicate rows found in the file")
                                .build());
            }

            List<UserDto> userDtos = userService.uploadUsers(userRequests);

            if (userDtos.size() > 0) {
                return ResponseEntity.status(HttpStatus.CREATED)
                        .body(new MessageResponse().builder()
                                .message("Data created or updated")
                                .build());
            } else {
                log.error("Failed to upload file");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new MessageResponse().builder()
                                .message("Failed to upload due to server error")
                                .build());
            }

        } catch (IOException e) {
            log.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new MessageResponse().builder()
                            .message("Unable to read the file. Please check file format")
                            .build());

        } catch (ParseException e) {
            log.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new MessageResponse().builder()
                            .message("Parsing Error. Please check date format")
                            .build());

        } catch (NumberFormatException e) {
            log.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new MessageResponse().builder()
                            .message("Parsing Error. Please check salary format")
                            .build());
        }
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> fetchUsers
            (@RequestParam(value = "minSalary", required = false) String minSalary,
             @RequestParam(value = "maxSalary", required = false) String maxSalary,
             @RequestParam(value = "offset", required = false) String offset,
             @RequestParam(value = "limit", required = false) String limit) {


        BigDecimal minSalaryDecimal;
        BigDecimal maxSalaryDecimal;
        Integer offsetInt ;
        Integer limitInt ;
        try {
            minSalaryDecimal = minSalary == null ? null : NumUtil.bigDecimal(minSalary);
            maxSalaryDecimal = maxSalary == null ? null : NumUtil.bigDecimal(maxSalary);
            offsetInt = offset == null ? null : Integer.parseInt(offset);
            limitInt = limit == null ? null : Integer.parseInt(limit);
        } catch(Exception e){
            log.error(e.getMessage(),e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        List<UserDto> userDtos = userService.fetchUsers(minSalaryDecimal, maxSalaryDecimal,
                offsetInt, limitInt);
        List<UserResponse> responses = userDtos.stream()
                        .map(u -> userMapper.mapUserDtoToUserResponse(u))
                        .collect(Collectors.toList());
        return ResponseEntity.ok().body(responses);

    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUser() {
        //TODO
        UserResponse response = new UserResponse();
        return ResponseEntity.ok().body(response);
    }

    @PostMapping
    public ResponseEntity<List<MessageResponse>> createUser() {
        //TODO
        List<MessageResponse> response = new ArrayList<>();
        return ResponseEntity.ok().body(response);
    }

    @PatchMapping
    public ResponseEntity<List<MessageResponse>> updateUser() {
        //TODO
        List<MessageResponse> responses = new ArrayList<>();
        return ResponseEntity.ok().body(responses);
    }

    @DeleteMapping
    public ResponseEntity<List<MessageResponse>> deleteUser() {
        //TODO
        List<MessageResponse> responses = new ArrayList<>();
        return ResponseEntity.ok().body(responses);
    }


}
