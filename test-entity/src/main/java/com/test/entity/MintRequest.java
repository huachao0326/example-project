package com.test.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * 铸币请求表
 *
 * @author huachao
 * @since 2022-03-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class MintRequest extends BaseEntity {

    /**
     * 链名称
     */
    @TableField("block_chain")
    public String blockChain;

    /**
     * 铸币数量
     */
    @TableField("amount")
    public BigDecimal amount;

    /**
     * policyId
     */
    @TableField("policy_id")
    public String policyId;

    /**
     * clientId
     */
    @TableField("client_id")
    public String clientId;

    /**
     * 资金账户
     */
    @TableField("source")
    public String source;

    /**
     * 签名信息
     */
    @TableField("sign_info")
    public String signInfo;

    /**
     * 币种
     */
    @TableField("currency")
    public String currency;

    /**
     * 交易序号
     */
    @TableField("tx_id")
    public String txId;

    /**
     * 交易状态
     */
    @TableField("tx_status")
    public int txStatus;

    /**
     * 交易hash
     */
    @TableField("tx_hash")
    public String txHash;

    /**
     * 错误码
     */
    @TableField("error_code")
    public int errorCode;

    /**
     * 错误信息
     */
    @TableField("error_message")
    public String errorMessage;

}
