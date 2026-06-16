package com.smartestate.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.smartestate.common.ErrorCode;
import com.smartestate.constants.LogTemplates;
import com.smartestate.constants.Messages;
import com.smartestate.constants.RepairConstants;
import com.smartestate.constants.UserConstants;
import com.smartestate.dto.RepairAssignRequest;
import com.smartestate.dto.RepairRequest;
import com.smartestate.dto.RepairStatusRequest;
import com.smartestate.entity.Repair;
import com.smartestate.entity.User;
import com.smartestate.mapper.RepairMapper;
import com.smartestate.mapper.UserMapper;
import com.smartestate.service.OperationLogService;
import com.smartestate.service.RepairService;
import com.smartestate.utils.CommonFormatter;
import com.smartestate.utils.LogUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class RepairServiceImpl implements RepairService {
    private final RepairMapper repairMapper;
    private final UserMapper userMapper;
    private final OperationLogService operationLogService;

    private static final Map<String, String> STATUS_TEMPLATE = Map.of(
            RepairConstants.PENDING, LogTemplates.REPAIR_STATUS_PENDING,
            RepairConstants.ASSIGNED, LogTemplates.REPAIR_STATUS_ASSIGNED,
            RepairConstants.PROCESSING, LogTemplates.REPAIR_STATUS_PROCESSING,
            RepairConstants.DONE, LogTemplates.REPAIR_STATUS_DONE,
            RepairConstants.CLOSED, LogTemplates.REPAIR_STATUS_CLOSED
    );

    public RepairServiceImpl(RepairMapper repairMapper, UserMapper userMapper, OperationLogService operationLogService) {
        this.repairMapper = repairMapper;
        this.userMapper = userMapper;
        this.operationLogService = operationLogService;
    }

    @Override
    public List<Repair> list(String status, String role) {
        LambdaQueryWrapper<Repair> wrapper = new LambdaQueryWrapper<Repair>().orderByDesc(Repair::getCreatedAt);
        if (StringUtils.hasText(status)) {
            wrapper.eq(Repair::getStatus, status);
        }
        List<Repair> repairs = repairMapper.selectList(wrapper);
        repairs.forEach(this::hydrate);
        LogUtil.info(LogTemplates.REPAIR_LIST, status == null ? "all" : status, role);
        return repairs;
    }

    @Override
    public Repair create(Long userId, String role, RepairRequest request) {
        Repair repair = new Repair();
        repair.setUserId(userId);
        repair.setTitle(request.getTitle());
        repair.setDescription(request.getDescription());
        repair.setType(StringUtils.hasText(request.getType()) ? request.getType() : "other");
        repair.setImages("");
        repair.setStatus(RepairConstants.PENDING);
        repair.setCreatedAt(LocalDateTime.now());
        repair.setUpdatedAt(LocalDateTime.now());
        repairMapper.insert(repair);
        operationLogService.record(userId, role, "repair.create", "Repair", repair.getId(),
                String.format(LogTemplates.REPAIR_CREATE, repair.getId(), role) + " | " + Messages.FRONT_REPAIR_CREATED);
        hydrate(repair);
        return repair;
    }

    @Override
    public Repair assign(Long id, String role, RepairAssignRequest request) {
        Repair repair = repairMapper.selectById(id);
        if (repair == null) {
            throw new IllegalArgumentException(ErrorCode.REPAIR_NOT_FOUND.format(id, role));
        }
        User handler = userMapper.selectById(request.getHandlerId());
        if (handler == null || !(UserConstants.STAFF.equals(handler.getRole()) || UserConstants.ADMIN.equals(handler.getRole()))) {
            throw new IllegalArgumentException(ErrorCode.REPAIR_HANDLER_INVALID.format(id, role));
        }
        repair.setHandlerId(request.getHandlerId());
        repair.setStatus(RepairConstants.ASSIGNED);
        repair.setUpdatedAt(LocalDateTime.now());
        repairMapper.updateById(repair);
        operationLogService.record(request.getHandlerId(), role, "repair.assign", "Repair", id,
                String.format(LogTemplates.REPAIR_ASSIGN, id, request.getHandlerId(), role) + " | " +
                        String.format(Messages.BACK_REPAIR_ASSIGNED, id, role));
        hydrate(repair);
        return repair;
    }

    @Override
    public Repair updateStatus(Long id, String role, RepairStatusRequest request) {
        if (!RepairConstants.ALL_STATUSES.contains(request.getStatus())) {
            throw new IllegalArgumentException(ErrorCode.REPAIR_STATUS_INVALID.format(request.getStatus(), role));
        }
        Repair repair = repairMapper.selectById(id);
        if (repair == null) {
            throw new IllegalArgumentException(ErrorCode.REPAIR_NOT_FOUND.format(id, role));
        }
        repair.setStatus(request.getStatus());
        repair.setUpdatedAt(LocalDateTime.now());
        repairMapper.updateById(repair);
        String template = STATUS_TEMPLATE.getOrDefault(request.getStatus(), LogTemplates.REPAIR_STATUS_PENDING);
        operationLogService.record(repair.getUserId(), role, "repair.status", "Repair", id,
                String.format(template, id, role) + " | formatter=" + CommonFormatter.repairStatusText(request.getStatus()));
        hydrate(repair);
        return repair;
    }

    private void hydrate(Repair repair) {
        if (repair.getUserId() != null) {
            User user = userMapper.selectById(repair.getUserId());
            if (user != null) user.setPasswordHash(null);
            repair.setUser(user);
        }
        if (repair.getHandlerId() != null) {
            User handler = userMapper.selectById(repair.getHandlerId());
            if (handler != null) handler.setPasswordHash(null);
            repair.setHandler(handler);
        }
    }
}
