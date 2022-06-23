package com.test.enums;

import lombok.Getter;

import java.util.Objects;

/**
 * @Description: 排序字段
 * @Author: hua_c
 * @Date: 2022-03-16
 **/

@Getter
public enum TradeStatus {
    /**
     *
     */
    TX_ERROR(0, "ERROR"),
    TX_SUCCESS(1, "SUCCESS"),
    TX_PENDING(2, "PENDING");

    private int code;
    private String value;

    TradeStatus(int code, String value) {
        this.code = code;
        this.value = value;
    }

    public static String getNameByCode(int code) {
        for (int i = 0; i < TradeStatus.values().length; i++) {
            TradeStatus dict = TradeStatus.values()[i];
            if (Objects.equals(dict.getCode(), code)) {
                return dict.getValue();
            }
        }
        return null;
    }

}
