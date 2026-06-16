package com.smartestate.controller;

import com.smartestate.common.Constants;
import com.smartestate.common.Result;
import com.smartestate.dto.AnnouncementRequest;
import com.smartestate.entity.Announcement;
import com.smartestate.service.AnnouncementService;
import com.smartestate.utils.LogUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/announcements")
public class AnnouncementController {
    private final AnnouncementService announcementService;

    public AnnouncementController(AnnouncementService announcementService) {
        this.announcementService = announcementService;
    }

    @GetMapping
    public Result<List<Announcement>> list(HttpServletRequest request) {
        String role = (String) request.getAttribute(Constants.CURRENT_USER_ROLE);
        LogUtil.info("AnnouncementController list role=%s", role);
        return Result.ok(announcementService.list(role));
    }

    @PostMapping
    public Result<Announcement> publish(@RequestBody AnnouncementRequest body, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute(Constants.CURRENT_USER_ID);
        String role = (String) request.getAttribute(Constants.CURRENT_USER_ROLE);
        LogUtil.info("AnnouncementController publish title=%s role=%s", body.getTitle(), role);
        return Result.ok(announcementService.publish(userId, role, body));
    }

    @PostMapping("/{id}/read")
    public Result<Announcement> read(@PathVariable Long id, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute(Constants.CURRENT_USER_ID);
        String role = (String) request.getAttribute(Constants.CURRENT_USER_ROLE);
        return Result.ok(announcementService.read(id, userId, role));
    }
}
