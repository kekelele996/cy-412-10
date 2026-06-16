package com.smartestate.constants;

import java.util.List;

public final class UserConstants {
    public static final String RESIDENT = "resident";
    public static final String STAFF = "staff";
    public static final String ADMIN = "admin";

    public static final List<String> ALL_ROLES = List.of(RESIDENT, STAFF, ADMIN);

    private UserConstants() {
    }
}
