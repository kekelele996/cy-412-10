package com.smartestate.interceptor;

import com.smartestate.common.Constants;
import com.smartestate.constants.LogTemplates;
import com.smartestate.utils.LogUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class OperationLogInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String role = String.valueOf(request.getAttribute(Constants.CURRENT_USER_ROLE));
        LogUtil.info(LogTemplates.INTERCEPTOR_ENTER, "OperationLogInterceptor", request.getRequestURI(), role);
        return true;
    }
}
