package com.test.vo;

import lombok.Data;

@Data
public class AccountGetVo {

    /**
     * 余额
     */
    public String balance;

    /**
     * 币种
     */
    public String currency;

    /**
     * 状态
     */
    public String state;

    /**
     * 冻结金额
     */
    public String suspense;

}
