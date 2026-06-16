package com.smartestate.interceptor;

import com.smartestate.common.Constants;
import com.smartestate.constants.LogTemplates;
import com.smartestate.constants.Messages;
import com.smartestate.constants.Permissions;
import com.smartestate.service.PermissionService;
import com.smartestate.utils.LogUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Map;

@Component
public class RbacInterceptor implements HandlerInterceptor {
    private final PermissionService permissionService;

    private static final Map<String, String> ROUTE_PERMISSIONS = Map.ofEntries(
            Map.entry("GET /users/profile", Permissions.USER_PROFILE),
            Map.entry("PUT /users/profile", Permissions.USER_PROFILE),
            Map.entry("GET /users/staff", Permissions.REPAIR_ASSIGN),
            Map.entry("GET /repairs", Permissions.REPAIR_VIEW),
            Map.entry("POST /repairs", Permissions.REPAIR_CREATE),
            Map.entry("PUT /repairs/.*/assign", Permissions.REPAIR_ASSIGN),
            Map.entry("PUT /repairs/.*/status", Permissions.REPAIR_UPDATE),
            Map.entry("GET /payments", Permissions.PAYMENT_VIEW),
            Map.entry("POST /payments/.*/pay", Permissions.PAYMENT_PAY),
            Map.entry("POST /payments/generate", Permissions.PAYMENT_VIEW),
            Map.entry("GET /announcements", Permissions.ANNOUNCEMENT_VIEW),
            Map.entry("POST /announcements", Permissions.ANNOUNCEMENT_PUBLISH),
            Map.entry("POST /announcements/.*/read", Permissions.ANNOUNCEMENT_VIEW),
            Map.entry("GET /operation-logs", Permissions.OPERATION_LOG_VIEW)
    );

    public RbacInterceptor(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String role = (String) request.getAttribute(Constants.CURRENT_USER_ROLE);
        String permission = permissionFor(request.getMethod(), request.getRequestURI());
        if (permission == null) {
            return true;
        }
        if (!permissionService.hasPermission(role, permission)) {
            LogUtil.warn(LogTemplates.RBAC_BLOCK, permission, role);
            LogUtil.warn(Messages.MIXED_PERMISSION_DENIED, permission, role);
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return false;
        }
        LogUtil.info(LogTemplates.RBAC_PASS, permission, role);
        return true;
    }

    private String permissionFor(String method, String uri) {
        String path = uri.replaceFirst("^/api", "");
        String key = method + " " + path;
        return ROUTE_PERMISSIONS.entrySet().stream()
                .filter(entry -> key.matches(entry.getKey()))
                .map(Map.Entry::getValue)
                .findFirst()
                .orElse(null);
    }
}
