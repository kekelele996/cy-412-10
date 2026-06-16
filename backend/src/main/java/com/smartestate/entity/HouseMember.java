package com.smartestate.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("house_members")
public class HouseMember {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String building;
    private String unit;
    private String room;
    private Long userId;
    private String relation;
    private Long invitedBy;
    private String status;
    private LocalDateTime createdAt;

    @TableField(exist = false)
    private User user;
}
