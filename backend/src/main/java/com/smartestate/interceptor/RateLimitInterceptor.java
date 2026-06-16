package com.smartestate.interceptor;

import com.smartestate.common.Constants;
import com.smartestate.common.ErrorCode;
import com.smartestate.config.RateLimitProperties;
import com.smartestate.constants.LogTemplates;
import com.smartestate.utils.LogUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RateLimitInterceptor implements HandlerInterceptor {
    private final RateLimitProperties properties;
    private final Map<String, Deque<Long>> buckets = new ConcurrentHashMap<>();

    public RateLimitInterceptor(RateLimitProperties properties) {
        this.properties = properties;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String ip = request.getRemoteAddr();
        long now = System.currentTimeMillis();
        long windowStart = now - properties.getWindowSeconds() * 1000L;
        Deque<Long> bucket = buckets.computeIfAbsent(ip, ignored -> new ArrayDeque<>());
        synchronized (bucket) {
            while (!bucket.isEmpty() && bucket.peekFirst() < windowStart) {
                bucket.removeFirst();
            }
            if (bucket.size() >= properties.getMaxRequests()) {
                String role = String.valueOf(request.getAttribute(Constants.CURRENT_USER_ROLE));
                LogUtil.warn(LogTemplates.RATE_LIMIT_BLOCK, ip, role);
                response.setStatus(429);
                try {
                    response.getWriter().write(ErrorCode.RATE_LIMITED.format(ip, role));
                } catch (Exception ignored) {
                }
                return false;
            }
            bucket.addLast(now);
        }
        LogUtil.info(LogTemplates.RATE_LIMIT_PASS, ip, String.valueOf(request.getAttribute(Constants.CURRENT_USER_ROLE)));
        return true;
    }
}
