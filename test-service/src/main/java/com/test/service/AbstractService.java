package com.test.service;

import java.util.Map;

/**
 * 功能描述:
 *
 * @author huachao
 * @create 2022/3/29 11:50
 * @since 1.0
 */
public abstract class AbstractService {

    public abstract Map<String, Object> getBurnAsset(String txHash);

    public abstract String signTransaction(String signInfo);

    public abstract String getAsset(String policyId, String assetName);

    public abstract Map<String, Object> deserialize(String signInfo);

}
