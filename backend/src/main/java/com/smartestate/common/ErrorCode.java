package com.smartestate.common;

public enum ErrorCode {
    UNAUTHORIZED("UserRole[unknown] auth failed: token missing"),
    FORBIDDEN("UserRole[%s] permission denied: permission_code=%s"),
    USER_NOT_FOUND("User[id=%s] lookup failed: current role=%s"),
    REPAIR_NOT_FOUND("Repair[id=%s] lookup failed: current role=%s"),
    REPAIR_STATUS_INVALID("Repair[status=%s] update failed: current role=%s"),
    REPAIR_HANDLER_INVALID("Repair[id=%s] assign failed: staff not found, current role=%s"),
    PAYMENT_NOT_FOUND("Payment[id=%s] lookup failed: current role=%s"),
    PAYMENT_ALREADY_PAID("Payment[id=%s] pay failed: status already paid, current role=%s"),
    ANNOUNCEMENT_NOT_FOUND("Announcement[id=%s] lookup failed: current role=%s"),
    RATE_LIMITED("Request[ip=%s] limited: current role=%s"),
    HOUSEHOLD_NOT_FOUND("HouseMember[id=%s] lookup failed: current role=%s"),
    HOUSEHOLD_ADD_FORBIDDEN("HouseMember[owner_id=%s] add forbidden: only resident can add, current role=%s"),
    HOUSEHOLD_USER_NOT_FOUND("HouseMember[phone=%s] user not found: current role=%s"),
    HOUSEHOLD_SELF_ADD("HouseMember[owner_id=%s] cannot add self: current role=%s"),
    HOUSEHOLD_ALREADY_EXISTS("HouseMember[user_id=%s] already in house: current role=%s"),
    HOUSEHOLD_REMOVE_FORBIDDEN("HouseMember[id=%s] remove forbidden: current role=%s"),
    SERVER_ERROR("SmartEstate server error: entity=%s field=%s current role=%s");

    private final String template;

    ErrorCode(String template) {
        this.template = template;
    }

    public String format(Object... args) {
        return String.format(template, args);
    }
}
