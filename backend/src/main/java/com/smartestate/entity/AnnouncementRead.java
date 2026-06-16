package com.smartestate.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("announcement_reads")
public class AnnouncementRead {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private Long announcementId;
    private LocalDateTime readAt;
}
