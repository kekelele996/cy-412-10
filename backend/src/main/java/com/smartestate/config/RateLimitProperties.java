package com.smartestate.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "smartestate.rate-limit")
public class RateLimitProperties {
    private int windowSeconds = 60;
    private int maxRequests = 80;
}
