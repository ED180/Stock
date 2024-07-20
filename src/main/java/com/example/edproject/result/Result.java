package com.example.edproject.result;

import lombok.Data;

@Data
public class Result<T> {
    private Integer code;
    private T data;
    private String message;

    public Result(Builder<T> builder) {
        this.message = builder.message;
        this.code = builder.code;
        this.data = builder.data;
    }

    public static class Builder<T> {
        private Integer code;
        private T data;
        private String message;

        public Builder<T> success() {
            this.code = ResultCode.SUCCESS.getCode();
            this.message = ResultCode.SUCCESS.getMessage();
            return this;
        }

        public Builder<T> fail(ResultCode resultCode) {
            this.code = resultCode.getCode();
            this.message = resultCode.getMessage();
            return this;
        }

        public Builder<T> fail(Integer code, String msg) {
            this.code = code;
            this.message = msg;
            return this;
        }

        public Builder<T> addData(T data) {
            this.data = data;
            return this;
        }

        public Result<T> build() {
            return new Result<>(this);
        }
    }

    public static <T> Builder<T> newBuilder() {
        return new Builder<>();
    }

    public static <T> Result<T> genSuccessResult(T data) {
        return Result.<T>newBuilder().success().addData(data).build();
    }

    public static <T> Result<T> genSuccessResult() {
        return Result.<T>newBuilder().success().build();
    }

    public static <T> Result<T> genFailResult(ResultCode resultCode) {
        return Result.<T>newBuilder().fail(resultCode).build();
    }
}
