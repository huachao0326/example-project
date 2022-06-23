package com.test.response;

import lombok.Getter;

/**
 * 错误码
 */
@Getter
public enum ErrorCode {

    TX_NOT_FOUND(100101, "交易不存在"),
    ACCOUNT_NOT_FOUND(100102, "账户不存在"),
    ACCOUNT_SIGN_ERROR(100103, "签名失败"),
    HOST_ANALYSIS_ERROR(100104, "域名解析失败"),
    QUERY_ASSET_ERROR(100105, "资产查询失败"),
    ASSET_OVER_ISSUE(100106, "资产超发"),
    CURRENCY_EXIST(100107, "币种已经存在"),
    AK_SK_NOT_EXIST(100108, "ak/sk不存在"),
    TRANSACTION_DESERIALIZE_ERROR(100109, "交易反序列化失败"),
    TRANSACTION_SIGN_ERROR(100110, "交易签名失败"),
    TRANSACTION_SUBMIT_ERROR(100111, "交易广播失败"),
    MINT_ASSET_EMPTY(100112, "铸币资产为空"),
    BURN_HASH_REPEAT(100113, "burnHash重复使用"),
    BURN_HASH_NON_EXISTENT(100114, "burnHash不存在或交易失败"),
    GET_TRANSACTION_API_EXCEPTION(100115, "查询交易失败"),
    NOT_SUPPORT_BLOCKCHAIN(100116, "不支持的链"),
    ;


    private int errorCode;
    private String errorMsg;

    ErrorCode(int errorCode, String errorMsg) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

}
