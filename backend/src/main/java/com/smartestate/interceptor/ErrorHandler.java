package com.smartestate.interceptor;

import com.smartestate.common.ErrorCode;
import com.smartestate.common.Result;
import com.smartestate.constants.LogTemplates;
import com.smartestate.utils.LogUtil;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ErrorHandler {
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<Void> handleIllegalArgument(IllegalArgumentException ex) {
        LogUtil.warn(LogTemplates.ERROR_WRAPPED, ErrorCode.SERVER_ERROR.name(), "controllerAdvice", "unknown");
        return Result.fail(ErrorCode.SERVER_ERROR, ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<Void> handleException(Exception ex) {
        LogUtil.error(LogTemplates.ERROR_WRAPPED, ErrorCode.SERVER_ERROR.name(), "controllerAdvice", "unknown");
        return Result.fail(ErrorCode.SERVER_ERROR, ex.getMessage());
    }
}
