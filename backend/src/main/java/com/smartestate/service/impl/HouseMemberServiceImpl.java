package com.smartestate.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.smartestate.common.ErrorCode;
import com.smartestate.constants.LogTemplates;
import com.smartestate.constants.UserConstants;
import com.smartestate.dto.HouseMemberAddRequest;
import com.smartestate.entity.HouseMember;
import com.smartestate.entity.User;
import com.smartestate.mapper.HouseMemberMapper;
import com.smartestate.mapper.UserMapper;
import com.smartestate.service.HouseMemberService;
import com.smartestate.service.OperationLogService;
import com.smartestate.utils.LogUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class HouseMemberServiceImpl implements HouseMemberService {
    private final HouseMemberMapper houseMemberMapper;
    private final UserMapper userMapper;
    private final OperationLogService operationLogService;

    public HouseMemberServiceImpl(HouseMemberMapper houseMemberMapper, UserMapper userMapper,
                                  OperationLogService operationLogService) {
        this.houseMemberMapper = houseMemberMapper;
        this.userMapper = userMapper;
        this.operationLogService = operationLogService;
    }

    @Override
    public List<HouseMember> listByHouse(Long userId, String role) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new IllegalArgumentException(ErrorCode.USER_NOT_FOUND.format(userId, role));
        }
        List<HouseMember> members = houseMemberMapper.selectList(
                new LambdaQueryWrapper<HouseMember>()
                        .eq(HouseMember::getBuilding, user.getBuilding())
                        .eq(HouseMember::getUnit, user.getUnit())
                        .eq(HouseMember::getRoom, user.getRoom())
                        .eq(HouseMember::getStatus, "active")
                        .orderByAsc(HouseMember::getRelation));
        members.forEach(this::hydrate);
        LogUtil.info(LogTemplates.HOUSEHOLD_LIST, user.getBuilding(), user.getUnit(), user.getRoom(), role);
        return members;
    }

    @Override
    public List<HouseMember> listAll(String role) {
        if (!UserConstants.STAFF.equals(role) && !UserConstants.ADMIN.equals(role)) {
            throw new IllegalArgumentException(ErrorCode.FORBIDDEN.format(role, "household:view"));
        }
        List<HouseMember> members = houseMemberMapper.selectList(
                new LambdaQueryWrapper<HouseMember>()
                        .eq(HouseMember::getStatus, "active")
                        .orderByAsc(HouseMember::getBuilding)
                        .orderByAsc(HouseMember::getUnit)
                        .orderByAsc(HouseMember::getRoom)
                        .orderByAsc(HouseMember::getRelation));
        members.forEach(this::hydrate);
        LogUtil.info(LogTemplates.HOUSEHOLD_LIST_ALL, role);
        return members;
    }

    @Override
    public HouseMember add(Long ownerUserId, String role, HouseMemberAddRequest request) {
        User owner = userMapper.selectById(ownerUserId);
        if (owner == null) {
            throw new IllegalArgumentException(ErrorCode.USER_NOT_FOUND.format(ownerUserId, role));
        }
        if (!UserConstants.RESIDENT.equals(owner.getRole())) {
            throw new IllegalArgumentException(ErrorCode.HOUSEHOLD_ADD_FORBIDDEN.format(ownerUserId, role));
        }
        User coResident = userMapper.selectOne(
                new LambdaQueryWrapper<User>().eq(User::getPhone, request.getPhone()));
        if (coResident == null) {
            throw new IllegalArgumentException(ErrorCode.HOUSEHOLD_USER_NOT_FOUND.format(request.getPhone(), role));
        }
        if (coResident.getId().equals(ownerUserId)) {
            throw new IllegalArgumentException(ErrorCode.HOUSEHOLD_SELF_ADD.format(ownerUserId, role));
        }
        LambdaQueryWrapper<HouseMember> existCheck = new LambdaQueryWrapper<HouseMember>()
                .eq(HouseMember::getBuilding, owner.getBuilding())
                .eq(HouseMember::getUnit, owner.getUnit())
                .eq(HouseMember::getRoom, owner.getRoom())
                .eq(HouseMember::getUserId, coResident.getId())
                .eq(HouseMember::getStatus, "active");
        if (houseMemberMapper.selectCount(existCheck) > 0) {
            throw new IllegalArgumentException(ErrorCode.HOUSEHOLD_ALREADY_EXISTS.format(coResident.getId(), role));
        }
        coResident.setRole(UserConstants.CO_RESIDENT);
        coResident.setBuilding(owner.getBuilding());
        coResident.setUnit(owner.getUnit());
        coResident.setRoom(owner.getRoom());
        userMapper.updateById(coResident);
        HouseMember member = new HouseMember();
        member.setBuilding(owner.getBuilding());
        member.setUnit(owner.getUnit());
        member.setRoom(owner.getRoom());
        member.setUserId(coResident.getId());
        member.setRelation("co-resident");
        member.setInvitedBy(ownerUserId);
        member.setStatus("active");
        member.setCreatedAt(LocalDateTime.now());
        houseMemberMapper.insert(member);
        operationLogService.record(ownerUserId, role, "household.add", "HouseMember", member.getId(),
                String.format(LogTemplates.HOUSEHOLD_ADD, coResident.getId(), owner.getBuilding(), owner.getUnit(), owner.getRoom(), role));
        hydrate(member);
        return member;
    }

    @Override
    public HouseMember remove(Long id, Long ownerUserId, String role) {
        HouseMember member = houseMemberMapper.selectById(id);
        if (member == null) {
            throw new IllegalArgumentException(ErrorCode.HOUSEHOLD_NOT_FOUND.format(id, role));
        }
        User owner = userMapper.selectById(ownerUserId);
        if (owner == null) {
            throw new IllegalArgumentException(ErrorCode.USER_NOT_FOUND.format(ownerUserId, role));
        }
        boolean isOwnerOfHouse = owner.getBuilding().equals(member.getBuilding())
                && owner.getUnit().equals(member.getUnit())
                && owner.getRoom().equals(member.getRoom())
                && UserConstants.RESIDENT.equals(owner.getRole());
        boolean isAdmin = UserConstants.ADMIN.equals(role);
        if (!isOwnerOfHouse && !isAdmin) {
            throw new IllegalArgumentException(ErrorCode.HOUSEHOLD_REMOVE_FORBIDDEN.format(id, role));
        }
        member.setStatus("removed");
        houseMemberMapper.updateById(member);
        User coResident = userMapper.selectById(member.getUserId());
        if (coResident != null) {
            coResident.setBuilding(null);
            coResident.setUnit(null);
            coResident.setRoom(null);
            coResident.setRole(UserConstants.RESIDENT);
            userMapper.updateById(coResident);
        }
        operationLogService.record(ownerUserId, role, "household.remove", "HouseMember", id,
                String.format(LogTemplates.HOUSEHOLD_REMOVE, member.getUserId(), role));
        hydrate(member);
        return member;
    }

    private void hydrate(HouseMember member) {
        if (member.getUserId() != null) {
            User user = userMapper.selectById(member.getUserId());
            if (user != null) user.setPasswordHash(null);
            member.setUser(user);
        }
    }
}
