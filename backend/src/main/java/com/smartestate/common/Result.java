package com.smartestate.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> {
    private boolean success;
    private String code;
    private String message;
    private T data;

    public static <T> Result<T> ok(T data) {
        return new Result<>(true, "OK", "success", data);
    }

    public static <T> Result<T> fail(ErrorCode code, String message) {
        return new Result<>(false, code.name(), message, null);
    }
}
