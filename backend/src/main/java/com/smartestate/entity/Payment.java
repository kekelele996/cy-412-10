package com.smartestate.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("payments")
public class Payment {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private String feeType;
    private BigDecimal amount;
    private String month;
    private String status;
    private LocalDateTime paidAt;
    private LocalDateTime createdAt;

    @TableField(exist = false)
    private User user;
}
