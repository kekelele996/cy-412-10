package com.smartestate.utils;

import com.smartestate.constants.RepairConstants;
import com.smartestate.constants.UserConstants;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class CommonFormatter {
    private static final DateTimeFormatter DATE_TIME = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private CommonFormatter() {
    }

    public static String date(LocalDateTime value) {
        return value == null ? "" : DATE_TIME.format(value);
    }

    public static String money(BigDecimal value) {
        return value == null ? "¥0.00" : "¥" + value.setScale(2).toPlainString();
    }

    public static String repairStatusText(String status) {
        return switch (status) {
            case RepairConstants.PENDING -> "待分配";
            case RepairConstants.ASSIGNED -> "已分配";
            case RepairConstants.PROCESSING -> "处理中";
            case RepairConstants.DONE -> "待确认";
            case RepairConstants.CLOSED -> "已关闭";
            default -> status;
        };
    }

    public static String roleText(String role) {
        return switch (role) {
            case UserConstants.RESIDENT -> "业主";
            case UserConstants.STAFF -> "物业";
            case UserConstants.ADMIN -> "管理员";
            default -> role;
        };
    }
}
