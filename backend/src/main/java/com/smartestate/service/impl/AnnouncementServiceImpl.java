package com.smartestate.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.smartestate.common.ErrorCode;
import com.smartestate.constants.LogTemplates;
import com.smartestate.dto.AnnouncementRequest;
import com.smartestate.entity.Announcement;
import com.smartestate.entity.AnnouncementRead;
import com.smartestate.mapper.AnnouncementMapper;
import com.smartestate.mapper.AnnouncementReadMapper;
import com.smartestate.service.AnnouncementService;
import com.smartestate.service.OperationLogService;
import com.smartestate.utils.LogUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AnnouncementServiceImpl implements AnnouncementService {
    private final AnnouncementMapper announcementMapper;
    private final AnnouncementReadMapper announcementReadMapper;
    private final OperationLogService operationLogService;

    public AnnouncementServiceImpl(AnnouncementMapper announcementMapper, AnnouncementReadMapper announcementReadMapper,
                                   OperationLogService operationLogService) {
        this.announcementMapper = announcementMapper;
        this.announcementReadMapper = announcementReadMapper;
        this.operationLogService = operationLogService;
    }

    @Override
    public List<Announcement> list(String role) {
        LogUtil.info(LogTemplates.ANNOUNCEMENT_LIST, role);
        return announcementMapper.selectList(new LambdaQueryWrapper<Announcement>().orderByDesc(Announcement::getTop).orderByDesc(Announcement::getPublishAt));
    }

    @Override
    public Announcement publish(Long publisherId, String role, AnnouncementRequest request) {
        Announcement announcement = new Announcement();
        announcement.setTitle(request.getTitle());
        announcement.setContent(request.getContent());
        announcement.setCategory(request.getCategory() == null ? "notice" : request.getCategory());
        announcement.setPublisherId(publisherId);
        announcement.setPublishAt(LocalDateTime.now());
        announcement.setTop(Boolean.TRUE.equals(request.getTop()));
        announcement.setReadCount(0);
        announcementMapper.insert(announcement);
        operationLogService.record(publisherId, role, "announcement.publish", "Announcement", announcement.getId(),
                String.format(LogTemplates.ANNOUNCEMENT_CREATE, announcement.getId(), announcement.getCategory(), role));
        return announcement;
    }

    @Override
    public Announcement read(Long id, Long userId, String role) {
        Announcement announcement = announcementMapper.selectById(id);
        if (announcement == null) {
            throw new IllegalArgumentException(ErrorCode.ANNOUNCEMENT_NOT_FOUND.format(id, role));
        }
        AnnouncementRead existing = announcementReadMapper.selectOne(new LambdaQueryWrapper<AnnouncementRead>()
                .eq(AnnouncementRead::getAnnouncementId, id)
                .eq(AnnouncementRead::getUserId, userId));
        if (existing == null) {
            AnnouncementRead read = new AnnouncementRead();
            read.setAnnouncementId(id);
            read.setUserId(userId);
            read.setReadAt(LocalDateTime.now());
            announcementReadMapper.insert(read);
            announcementMapper.update(null, new LambdaUpdateWrapper<Announcement>()
                    .eq(Announcement::getId, id)
                    .setSql("read_count = read_count + 1"));
            announcement.setReadCount(announcement.getReadCount() + 1);
        }
        operationLogService.record(userId, role, "announcement.read", "Announcement", id,
                String.format(LogTemplates.ANNOUNCEMENT_READ, id, userId, role));
        return announcement;
    }
}
