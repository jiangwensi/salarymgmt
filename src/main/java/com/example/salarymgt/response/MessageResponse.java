package com.example.salarymgt.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by Jiang Wensi on 11/12/2020
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MessageResponse {
    private String message;
}
