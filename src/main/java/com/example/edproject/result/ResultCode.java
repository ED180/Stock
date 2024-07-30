package com.example.edproject.result;

public enum ResultCode {
    SUCCESS(1, "SUCCESS"),
    FAIL(0, "FAIL"),
    ;

    private final Integer code;
    private final String message;

    ResultCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
