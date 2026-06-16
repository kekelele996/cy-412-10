package com.smartestate.constants;

import java.util.List;

public final class RepairConstants {
    public static final String PENDING = "pending";
    public static final String ASSIGNED = "assigned";
    public static final String PROCESSING = "processing";
    public static final String DONE = "done";
    public static final String CLOSED = "closed";

    public static final List<String> ALL_STATUSES = List.of(PENDING, ASSIGNED, PROCESSING, DONE, CLOSED);

    private RepairConstants() {
    }
}
