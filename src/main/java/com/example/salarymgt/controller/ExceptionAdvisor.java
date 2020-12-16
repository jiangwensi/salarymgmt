package com.example.salarymgt.controller;

import com.example.salarymgt.response.MessageResponse;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jiang Wensi on 15/12/2020
 */
@ControllerAdvice
public class ExceptionAdvisor {

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public MessageResponse handleException(HttpMessageNotReadableException ex) {
        System.out.println(ex);
        Throwable cause = ex.getCause();

        if(cause instanceof InvalidFormatException){
            InvalidFormatException jpe = (InvalidFormatException)cause;
            if(jpe.getPath().toString().equals("[com.example.salarymgt.request.UserRequest[\"salary\"]]")){
                return new MessageResponse("Invalid salary");
            }
            if(jpe.getPath().toString().equals("[com.example.salarymgt.request.UserRequest[\"startDate\"]]")){
                return new MessageResponse("Invalid date");
            }
        }

       String path= ex.getHttpInputMessage().getHeaders().getLocation().getPath();
//        List<FieldError> errors = ex.getBindingResult().getFieldErrors();

        MessageResponse messageResponse=new MessageResponse();
//        if(errors!=null&& errors.size()>0){
//            FieldError fieldError = errors.get(0);
//            if(fieldError.getField().equals("salary")){
//                messageResponse.setMessage("Invalid salary");
//            }
//            if(fieldError.getField().equals("startDate")){
//                messageResponse.setMessage("Invalid date");
//            }
//        }

        return new MessageResponse("Invalid input");
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<MessageResponse> handleException(Exception exception) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new MessageResponse().builder().message(exception.getMessage())
                        .build());
    }
}
