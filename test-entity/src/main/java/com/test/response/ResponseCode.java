package com.test.response;

import lombok.Getter;

@Getter
public enum ResponseCode {

    SUCCESS(200, "调用成功"),
    ERROR(400, "很抱歉,系统发生故障");

    private Integer code;
    private String message;

    ResponseCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
