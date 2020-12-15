package com.example.salarymgt.controller;

import com.example.salarymgt.request.UserRequest;
import com.example.salarymgt.response.MessageResponse;
import com.example.salarymgt.response.UserResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jiang Wensi on 11/12/2020
 */
@RestController
@RequestMapping("users")
@AllArgsConstructor
@Slf4j
public class UserController {

//    private UserService userService;

    @PostMapping("/upload")
    public ResponseEntity<MessageResponse> uploadUser(@RequestBody List<UserRequest> userRequests) {

//        int updateCount = 0;
//        MessageResponse response = new MessageResponse();;
//        try{
//            userService.uploadUser(userRequests);
//        } catch(InvalidInputException e) {
//            log.error(e.getMessage(),e);
//            response.setMessage("Invalid user input");
//            return ResponseEntity.badRequest().body(response);
//        } catch (Exception e){
//            log.error(e.getMessage(),e);
//            response.setMessage("Server error");
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
//        }
//
//        if(updateCount==0){
//            response.setMessage("Success but no data updated.");
//            return ResponseEntity.status(HttpStatus.OK).body(response);
//        } else {
//            response.setMessage("Data created or uploaded.");
//            return ResponseEntity.status(HttpStatus.CREATED).body(response);
//        }
        return null;
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> fetchUsers() {

        //TODO
        List<UserResponse> responses = new ArrayList<>();
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
