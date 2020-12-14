package com.example.salarymgt.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Created by Jiang Wensi on 11/12/2020
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRequest {

    private String id;
    private String login;
    private String name;
    private BigDecimal salary;
    private String startDate;
}
