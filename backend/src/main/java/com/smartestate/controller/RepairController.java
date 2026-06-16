package com.smartestate.controller;

import com.smartestate.common.Constants;
import com.smartestate.common.Result;
import com.smartestate.dto.RepairAssignRequest;
import com.smartestate.dto.RepairRequest;
import com.smartestate.dto.RepairStatusRequest;
import com.smartestate.entity.Repair;
import com.smartestate.service.RepairService;
import com.smartestate.utils.LogUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/repairs")
public class RepairController {
    private final RepairService repairService;

    public RepairController(RepairService repairService) {
        this.repairService = repairService;
    }

    @GetMapping
    public Result<List<Repair>> list(@RequestParam(required = false) String status, HttpServletRequest request) {
        String role = (String) request.getAttribute(Constants.CURRENT_USER_ROLE);
        LogUtil.info("RepairController list status=%s role=%s", status, role);
        return Result.ok(repairService.list(status, role));
    }

    @PostMapping
    public Result<Repair> create(@RequestBody RepairRequest body, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute(Constants.CURRENT_USER_ID);
        String role = (String) request.getAttribute(Constants.CURRENT_USER_ROLE);
        LogUtil.info("RepairController create title=%s role=%s", body.getTitle(), role);
        return Result.ok(repairService.create(userId, role, body));
    }

    @PutMapping("/{id}/assign")
    public Result<Repair> assign(@PathVariable Long id, @RequestBody RepairAssignRequest body, HttpServletRequest request) {
        String role = (String) request.getAttribute(Constants.CURRENT_USER_ROLE);
        LogUtil.info("RepairController assign repair_id=%s role=%s", id, role);
        return Result.ok(repairService.assign(id, role, body));
    }

    @PutMapping("/{id}/status")
    public Result<Repair> status(@PathVariable Long id, @RequestBody RepairStatusRequest body, HttpServletRequest request) {
        String role = (String) request.getAttribute(Constants.CURRENT_USER_ROLE);
        LogUtil.info("RepairController status repair_id=%s status=%s role=%s", id, body.getStatus(), role);
        return Result.ok(repairService.updateStatus(id, role, body));
    }
}
