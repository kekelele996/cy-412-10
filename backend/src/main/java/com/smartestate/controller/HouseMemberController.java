package com.smartestate.controller;

import com.smartestate.common.Constants;
import com.smartestate.common.Result;
import com.smartestate.dto.HouseMemberAddRequest;
import com.smartestate.entity.HouseMember;
import com.smartestate.service.HouseMemberService;
import com.smartestate.utils.LogUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/household")
public class HouseMemberController {
    private final HouseMemberService houseMemberService;

    public HouseMemberController(HouseMemberService houseMemberService) {
        this.houseMemberService = houseMemberService;
    }

    @GetMapping
    public Result<List<HouseMember>> myHousehold(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute(Constants.CURRENT_USER_ID);
        String role = (String) request.getAttribute(Constants.CURRENT_USER_ROLE);
        LogUtil.info("HouseMemberController myHousehold user_id=%s role=%s", userId, role);
        return Result.ok(houseMemberService.listByHouse(userId, role));
    }

    @GetMapping("/all")
    public Result<List<HouseMember>> allHouseholds(HttpServletRequest request) {
        String role = (String) request.getAttribute(Constants.CURRENT_USER_ROLE);
        LogUtil.info("HouseMemberController allHouseholds role=%s", role);
        return Result.ok(houseMemberService.listAll(role));
    }

    @PostMapping
    public Result<HouseMember> add(@RequestBody HouseMemberAddRequest body, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute(Constants.CURRENT_USER_ID);
        String role = (String) request.getAttribute(Constants.CURRENT_USER_ROLE);
        LogUtil.info("HouseMemberController add phone=%s role=%s", body.getPhone(), role);
        return Result.ok(houseMemberService.add(userId, role, body));
    }

    @DeleteMapping("/{id}")
    public Result<HouseMember> remove(@PathVariable Long id, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute(Constants.CURRENT_USER_ID);
        String role = (String) request.getAttribute(Constants.CURRENT_USER_ROLE);
        LogUtil.info("HouseMemberController remove id=%s role=%s", id, role);
        return Result.ok(houseMemberService.remove(id, userId, role));
    }
}
