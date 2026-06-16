package com.smartestate.controller;

import com.smartestate.common.Result;
import com.smartestate.dto.LoginRequest;
import com.smartestate.dto.LoginResponse;
import com.smartestate.service.UserService;
import com.smartestate.utils.LogUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public Result<LoginResponse> login(@RequestBody LoginRequest request) {
        LogUtil.info("AuthController login phone=%s", request.getPhone());
        return Result.ok(userService.login(request));
    }
}
