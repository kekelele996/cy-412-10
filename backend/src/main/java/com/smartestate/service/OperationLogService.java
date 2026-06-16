package com.smartestate.service;

import com.smartestate.entity.OperationLog;

import java.util.List;

public interface OperationLogService {
    void record(Long userId, String role, String action, String entityName, Long entityId, String message);
    List<OperationLog> latest(String role);
}
