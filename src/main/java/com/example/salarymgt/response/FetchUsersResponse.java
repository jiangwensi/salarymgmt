package com.example.salarymgt.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Created by Jiang Wensi on 16/12/2020
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FetchUsersResponse {
    private List<UserResponse> results;
}
