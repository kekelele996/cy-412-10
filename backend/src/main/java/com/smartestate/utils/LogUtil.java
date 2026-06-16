package com.smartestate.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class LogUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger("SmartEstateOperation");

    private LogUtil() {
    }

    public static void info(String template, Object... args) {
        LOGGER.info(String.format(template, args));
    }

    public static void warn(String template, Object... args) {
        LOGGER.warn(String.format(template, args));
    }

    public static void error(String template, Object... args) {
        LOGGER.error(String.format(template, args));
    }
}
