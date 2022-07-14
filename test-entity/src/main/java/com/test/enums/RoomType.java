package com.test.enums;

import lombok.Getter;

/**
 * @Description: room用途
 * @Author: huaChao
 * @Date: 2022/6/23
 * @Version: 1.0.0
 **/
@Getter
public enum RoomType {

    KEYGEN("keygen", "keygen"),
    SIGN("sign", "sign"),
    RECONSTRUCT("reconstruct", "reconstruct"),
    ;
    private String typeName;
    private String typeCode;

    RoomType(String typeName, String typeCode) {
        this.typeName = typeName;
        this.typeCode = typeCode;
    }

    public static RoomType getRoomTypeConstant(String typeName) {
        for (RoomType constant : RoomType.values()) {
            if (constant.getTypeName().toLowerCase().equals(typeName.toLowerCase())) {
                return constant;
            }
        }
        return null;
    }

}
