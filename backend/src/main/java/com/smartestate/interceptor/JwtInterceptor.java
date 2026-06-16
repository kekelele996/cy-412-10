package com.smartestate.interceptor;

import com.smartestate.common.Constants;
import com.smartestate.constants.LogTemplates;
import com.smartestate.utils.JwtUtil;
import com.smartestate.utils.LogUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Map;

@Component
public class JwtInterceptor implements HandlerInterceptor {
    private final JwtUtil jwtUtil;

    public JwtInterceptor(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String auth = request.getHeader("Authorization");
        if (auth == null || !auth.startsWith(Constants.TOKEN_PREFIX)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
        Map<String, String> claims = jwtUtil.verify(auth.substring(Constants.TOKEN_PREFIX.length()));
        request.setAttribute(Constants.CURRENT_USER_ID, Long.parseLong(claims.get("id")));
        request.setAttribute(Constants.CURRENT_USER_PHONE, claims.get("phone"));
        request.setAttribute(Constants.CURRENT_USER_ROLE, claims.get("role"));
        LogUtil.info(LogTemplates.INTERCEPTOR_ENTER, "JwtInterceptor", request.getRequestURI(), claims.get("role"));
        return true;
    }
}
