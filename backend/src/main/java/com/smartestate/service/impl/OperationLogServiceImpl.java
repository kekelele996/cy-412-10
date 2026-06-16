package com.smartestate.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.smartestate.constants.LogTemplates;
import com.smartestate.constants.UserConstants;
import com.smartestate.entity.OperationLog;
import com.smartestate.mapper.OperationLogMapper;
import com.smartestate.service.OperationLogService;
import com.smartestate.utils.LogUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OperationLogServiceImpl implements OperationLogService {
    private final OperationLogMapper operationLogMapper;

    public OperationLogServiceImpl(OperationLogMapper operationLogMapper) {
        this.operationLogMapper = operationLogMapper;
    }

    @Override
    public void record(Long userId, String role, String action, String entityName, Long entityId, String message) {
        OperationLog log = new OperationLog();
        log.setUserId(userId);
        log.setRole(role);
        log.setAction(action);
        log.setEntityName(entityName);
        log.setEntityId(entityId);
        log.setMessage(message);
        log.setCreatedAt(LocalDateTime.now());
        operationLogMapper.insert(log);
        LogUtil.info("%s", message);
    }

    @Override
    public List<OperationLog> latest(String role) {
        LogUtil.info(LogTemplates.DASHBOARD_STATS, role);
        if (!UserConstants.ADMIN.equals(role)) {
            return List.of();
        }
        return operationLogMapper.selectList(new LambdaQueryWrapper<OperationLog>().orderByDesc(OperationLog::getCreatedAt).last("limit 20"));
    }
}
