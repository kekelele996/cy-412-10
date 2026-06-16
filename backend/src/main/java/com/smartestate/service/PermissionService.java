package com.smartestate.service;

import java.util.List;

public interface PermissionService {
    List<String> permissionsForRole(String role);
    boolean hasPermission(String role, String permission);
}
