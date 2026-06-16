package com.smartestate.dto;

import com.smartestate.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class LoginResponse {
    private String token;
    private User user;
    private List<String> permissions;
}
