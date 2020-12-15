package com.example.salarymgt.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ExceptionHandler.class)
class ExceptionHandlerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    void handleException() throws Exception {
        mockMvc.perform(get("invalid")).andExpect(status().is(500));
    }
}