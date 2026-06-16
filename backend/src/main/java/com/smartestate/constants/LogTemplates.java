package com.smartestate.constants;

public final class LogTemplates {
    public static final String AUTH_LOGIN = "User[phone=%s] login success: role=%s";
    public static final String AUTH_FAIL = "User[phone=%s] login failed: role=unknown";
    public static final String USER_PROFILE_READ = "User[id=%s] read profile: role=%s";
    public static final String USER_PROFILE_UPDATE = "User[id=%s] update profile: field=%s role=%s";
    public static final String USER_STAFF_LIST = "User[role=staff] list fetched by role=%s";
    public static final String REPAIR_LIST = "Repair[status=%s] list fetched by role=%s";
    public static final String REPAIR_CREATE = "Repair[id=%s] created: status=pending role=%s";
    public static final String REPAIR_ASSIGN = "Repair[id=%s] assigned: handler_id=%s role=%s";
    public static final String REPAIR_STATUS_PENDING = "Repair[id=%s] status changed to pending by role=%s";
    public static final String REPAIR_STATUS_ASSIGNED = "Repair[id=%s] status changed to assigned by role=%s";
    public static final String REPAIR_STATUS_PROCESSING = "Repair[id=%s] status changed to processing by role=%s";
    public static final String REPAIR_STATUS_DONE = "Repair[id=%s] status changed to done by role=%s";
    public static final String REPAIR_STATUS_CLOSED = "Repair[id=%s] status changed to closed by role=%s";
    public static final String REPAIR_RATING = "Repair[id=%s] rated: rating=%s role=%s";
    public static final String PAYMENT_LIST = "Payment[user_id=%s] list fetched by role=%s";
    public static final String PAYMENT_GENERATE = "Payment[id=%s] generated: fee_type=%s role=%s";
    public static final String PAYMENT_PAY = "Payment[id=%s] paid: month=%s role=%s";
    public static final String PAYMENT_DUPLICATE = "Payment[id=%s] duplicate payment blocked role=%s";
    public static final String ANNOUNCEMENT_LIST = "Announcement[list] fetched by role=%s";
    public static final String ANNOUNCEMENT_CREATE = "Announcement[id=%s] created: category=%s role=%s";
    public static final String ANNOUNCEMENT_READ = "Announcement[id=%s] read_count increased by user_id=%s role=%s";
    public static final String ANNOUNCEMENT_TOP = "Announcement[id=%s] top=%s role=%s";
    public static final String RBAC_PASS = "RBAC[permission=%s] passed for role=%s";
    public static final String RBAC_BLOCK = "RBAC[permission=%s] blocked for role=%s";
    public static final String RATE_LIMIT_PASS = "RateLimit[ip=%s] passed with role=%s";
    public static final String RATE_LIMIT_BLOCK = "RateLimit[ip=%s] blocked with role=%s";
    public static final String INTERCEPTOR_ENTER = "Interceptor[%s] entered: uri=%s role=%s";
    public static final String ERROR_WRAPPED = "Error[code=%s] wrapped at layer=%s role=%s";
    public static final String DASHBOARD_STATS = "Dashboard[repair/payment/announcement] stats queried by role=%s";
    public static final String FORMATTER_STATUS = "Formatter[RepairStatus=%s] rendered for role=%s";
    public static final String HOUSEHOLD_LIST = "HouseMember[building=%s unit=%s room=%s] list fetched by role=%s";
    public static final String HOUSEHOLD_LIST_ALL = "HouseMember[all] list fetched by role=%s";
    public static final String HOUSEHOLD_ADD = "HouseMember[user_id=%s] added to building=%s unit=%s room=%s by role=%s";
    public static final String HOUSEHOLD_REMOVE = "HouseMember[user_id=%s] removed by role=%s";

    private LogTemplates() {
    }
}
