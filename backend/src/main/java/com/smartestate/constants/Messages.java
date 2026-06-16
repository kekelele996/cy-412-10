package com.smartestate.constants;

public final class Messages {
    public static final String FRONT_REPAIR_CREATED = "报修已提交，物业将在 30 分钟内分配处理人";
    public static final String FRONT_REPAIR_UPDATED = "工单状态已同步";
    public static final String BACK_REPAIR_ASSIGNED = "Repair[id=%s] handler_id changed by role=%s";
    public static final String BACK_PAYMENT_PAID = "Payment[id=%s] sandbox paid by role=%s";
    public static final String LOG_USER_PROFILE = "User[id=%s] profile updated: field=building/unit/room role=%s";
    public static final String LOG_ANNOUNCEMENT_TOP = "Announcement[id=%s] top flag changed: role=%s";
    public static final String MIXED_PERMISSION_DENIED = "前端按钮隐藏与后端 RBAC 同时阻止 permission=%s role=%s";

    private Messages() {
    }
}
