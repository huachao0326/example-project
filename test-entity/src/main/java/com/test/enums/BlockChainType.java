package com.test.enums;

import lombok.Getter;

/**
 * @Description: 链名称
 * @Author: hua_c
 * @Date: 2022-03-16
 **/
@Getter
public enum BlockChainType {

    CARDANO("Cardano", "ADA"),
    ;
    private String blockChainName;
    private String blockChainCurrency;

    BlockChainType(String blockChainName, String blockChainCurrency) {
        this.blockChainName = blockChainName;
        this.blockChainCurrency = blockChainCurrency;
    }

    public static BlockChainType getBlockChainConstant(String blockChainName) {
        for (BlockChainType constant : BlockChainType.values()) {
            if (constant.getBlockChainName().toLowerCase().equals(blockChainName.toLowerCase())) {
                return constant;
            }
        }
        return null;
    }

}
