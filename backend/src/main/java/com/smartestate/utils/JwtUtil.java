package com.smartestate.utils;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.date.DateUtil;
import cn.hutool.crypto.SecureUtil;
import com.smartestate.common.ErrorCode;
import com.smartestate.entity.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {
    @Value("${smartestate.jwt-secret}")
    private String secret;

    public String createToken(User user) {
        long exp = DateUtil.offsetHour(new Date(), 12).getTime();
        String payload = user.getId() + "|" + user.getPhone() + "|" + user.getRole() + "|" + exp;
        String signature = sign(payload);
        return Base64.encode(payload + "|" + signature, StandardCharsets.UTF_8);
    }

    public Map<String, String> verify(String token) {
        try {
            String raw = Base64.decodeStr(token, StandardCharsets.UTF_8);
            String[] parts = raw.split("\\|");
            if (parts.length != 5 || !sign(parts[0] + "|" + parts[1] + "|" + parts[2] + "|" + parts[3]).equals(parts[4])) {
                throw new IllegalArgumentException(ErrorCode.UNAUTHORIZED.format());
            }
            if (Long.parseLong(parts[3]) < System.currentTimeMillis()) {
                throw new IllegalArgumentException(ErrorCode.UNAUTHORIZED.format());
            }
            Map<String, String> claims = new HashMap<>();
            claims.put("id", parts[0]);
            claims.put("phone", parts[1]);
            claims.put("role", parts[2]);
            return claims;
        } catch (Exception ex) {
            throw new IllegalArgumentException(ErrorCode.UNAUTHORIZED.format(), ex);
        }
    }

    private String sign(String payload) {
        return SecureUtil.sha256(payload + secret);
    }
}
