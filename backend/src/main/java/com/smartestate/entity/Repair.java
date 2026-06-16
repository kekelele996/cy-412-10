package com.smartestate.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.smartestate.constants.RepairConstants;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("repairs")
public class Repair {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private String title;
    private String description;
    private String type;
    private String images;
    private String status = RepairConstants.PENDING;
    private Long handlerId;
    private Integer rating;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @TableField(exist = false)
    private User user;
    @TableField(exist = false)
    private User handler;
}
