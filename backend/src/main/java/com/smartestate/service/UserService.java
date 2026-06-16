package com.smartestate.service;

import com.smartestate.dto.LoginRequest;
import com.smartestate.dto.LoginResponse;
import com.smartestate.dto.ProfileUpdateRequest;
import com.smartestate.entity.User;

import java.util.List;

public interface UserService {
    LoginResponse login(LoginRequest request);
    User profile(Long userId, String role);
    User updateProfile(Long userId, String role, ProfileUpdateRequest request);
    List<User> staff(String role);
}
