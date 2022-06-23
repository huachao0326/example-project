package com.test.util;


import java.util.UUID;

/**
 * @program: Dam
 * @description: UUID工具类
 * @author: hua_c
 * @create: 2022-02-22 01:13:10
 **/
public class UUIDGenerator {
    /***
     * 功能描述: 获取32UUID
     * @author hua_c
     * @date 2019/10/30
     * @param
     * @return
     */
    public static String get32UUID() {
        return UUID.randomUUID().toString().trim().replaceAll("-", "");
    }

}