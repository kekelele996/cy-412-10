package com.smartestate.service.impl;

import com.smartestate.constants.Permissions;
import com.smartestate.constants.UserConstants;
import com.smartestate.service.PermissionService;
import com.smartestate.utils.CommonFormatter;
import com.smartestate.utils.LogUtil;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class PermissionServiceImpl implements PermissionService {
    private static final Map<String, List<String>> ROLE_PERMISSIONS = Map.of(
            UserConstants.RESIDENT, List.of(
                    Permissions.DASHBOARD_VIEW,
                    Permissions.REPAIR_VIEW,
                    Permissions.REPAIR_CREATE,
                    Permissions.PAYMENT_VIEW,
                    Permissions.PAYMENT_PAY,
                    Permissions.ANNOUNCEMENT_VIEW,
                    Permissions.USER_PROFILE
            ),
            UserConstants.STAFF, List.of(
                    Permissions.DASHBOARD_VIEW,
                    Permissions.REPAIR_VIEW,
                    Permissions.REPAIR_ASSIGN,
                    Permissions.REPAIR_UPDATE,
                    Permissions.PAYMENT_VIEW,
                    Permissions.ANNOUNCEMENT_VIEW,
                    Permissions.ANNOUNCEMENT_PUBLISH,
                    Permissions.USER_PROFILE
            ),
            UserConstants.ADMIN, List.of(
                    Permissions.DASHBOARD_VIEW,
                    Permissions.REPAIR_VIEW,
                    Permissions.REPAIR_CREATE,
                    Permissions.REPAIR_ASSIGN,
                    Permissions.REPAIR_UPDATE,
                    Permissions.PAYMENT_VIEW,
                    Permissions.PAYMENT_PAY,
                    Permissions.ANNOUNCEMENT_VIEW,
                    Permissions.ANNOUNCEMENT_PUBLISH,
                    Permissions.USER_PROFILE,
                    Permissions.OPERATION_LOG_VIEW
            )
    );

    @Override
    public List<String> permissionsForRole(String role) {
        LogUtil.info("PermissionService role=%s rendered=%s", role, CommonFormatter.roleText(role));
        return ROLE_PERMISSIONS.getOrDefault(role, List.of());
    }

    @Override
    public boolean hasPermission(String role, String permission) {
        return permissionsForRole(role).contains(permission);
    }
}
