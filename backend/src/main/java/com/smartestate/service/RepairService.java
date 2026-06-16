package com.smartestate.service;

import com.smartestate.dto.RepairAssignRequest;
import com.smartestate.dto.RepairRequest;
import com.smartestate.dto.RepairStatusRequest;
import com.smartestate.entity.Repair;

import java.util.List;

public interface RepairService {
    List<Repair> list(String status, String role);
    Repair create(Long userId, String role, RepairRequest request);
    Repair assign(Long id, String role, RepairAssignRequest request);
    Repair updateStatus(Long id, String role, RepairStatusRequest request);
}
