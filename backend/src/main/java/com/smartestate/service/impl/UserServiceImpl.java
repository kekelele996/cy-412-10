package com.smartestate.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.smartestate.common.ErrorCode;
import com.smartestate.constants.LogTemplates;
import com.smartestate.constants.UserConstants;
import com.smartestate.dto.LoginRequest;
import com.smartestate.dto.LoginResponse;
import com.smartestate.dto.ProfileUpdateRequest;
import com.smartestate.entity.User;
import com.smartestate.mapper.UserMapper;
import com.smartestate.service.OperationLogService;
import com.smartestate.service.PermissionService;
import com.smartestate.service.UserService;
import com.smartestate.utils.CommonFormatter;
import com.smartestate.utils.JwtUtil;
import com.smartestate.utils.LogUtil;
import com.smartestate.utils.PasswordUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;
    private final JwtUtil jwtUtil;
    private final PermissionService permissionService;
    private final OperationLogService operationLogService;

    public UserServiceImpl(UserMapper userMapper, JwtUtil jwtUtil, PermissionService permissionService,
                           OperationLogService operationLogService) {
        this.userMapper = userMapper;
        this.jwtUtil = jwtUtil;
        this.permissionService = permissionService;
        this.operationLogService = operationLogService;
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getPhone, request.getPhone()));
        if (user == null || !PasswordUtil.matches(request.getPassword(), user.getPasswordHash())) {
            LogUtil.warn(LogTemplates.AUTH_FAIL, request.getPhone());
            throw new IllegalArgumentException(ErrorCode.UNAUTHORIZED.format());
        }
        user.setPasswordHash(null);
        String token = jwtUtil.createToken(user);
        LogUtil.info(LogTemplates.AUTH_LOGIN, user.getPhone(), user.getRole());
        operationLogService.record(user.getId(), user.getRole(), "auth.login", "User", user.getId(),
                String.format(LogTemplates.AUTH_LOGIN, user.getPhone(), user.getRole()));
        return new LoginResponse(token, user, permissionService.permissionsForRole(user.getRole()));
    }

    @Override
    public User profile(Long userId, String role) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new IllegalArgumentException(ErrorCode.USER_NOT_FOUND.format(userId, role));
        }
        user.setPasswordHash(null);
        LogUtil.info(LogTemplates.USER_PROFILE_READ, userId, CommonFormatter.roleText(role));
        return user;
    }

    @Override
    public User updateProfile(Long userId, String role, ProfileUpdateRequest request) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new IllegalArgumentException(ErrorCode.USER_NOT_FOUND.format(userId, role));
        }
        if (StringUtils.hasText(request.getNickname())) user.setNickname(request.getNickname());
        if (StringUtils.hasText(request.getAvatar())) user.setAvatar(request.getAvatar());
        user.setBuilding(request.getBuilding());
        user.setUnit(request.getUnit());
        user.setRoom(request.getRoom());
        userMapper.updateById(user);
        operationLogService.record(userId, role, "user.profile.update", "User", userId,
                String.format(LogTemplates.USER_PROFILE_UPDATE, userId, "nickname/avatar/building/unit/room", role));
        user.setPasswordHash(null);
        return user;
    }

    @Override
    public List<User> staff(String role) {
        LogUtil.info(LogTemplates.USER_STAFF_LIST, role);
        List<User> users = userMapper.selectList(new LambdaQueryWrapper<User>().in(User::getRole, UserConstants.STAFF, UserConstants.ADMIN));
        users.forEach(user -> user.setPasswordHash(null));
        return users;
    }
}
