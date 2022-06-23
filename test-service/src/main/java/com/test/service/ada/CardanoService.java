package com.test.service.ada;

import com.test.service.AbstractService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 功能描述:铸币请求 服务实现类
 *
 * @author hua_chao
 * @create 2022/3/18 下午3:58
 * @since 1.0.0
 */
@Service("CardanoService")
@Slf4j
public class CardanoService extends AbstractService {

    /**
     * 签名交易
     *
     * @return
     * @throws Exception
     */
    @Override
    public String signTransaction(String signInfo) {
        return "";
    }

    /**
     * 反序列化签名信息
     *
     * @param signInfo
     * @return
     */
    @Override
    public Map<String, Object> deserialize(String signInfo) {
        return new HashMap<>();
    }

    /**
     * 根据交易hash查询交易是否成功
     *
     * @param txHash
     */
    public boolean getTransaction(String txHash) {
        return false;
    }

    /**
     * 根据销毁hash获取销毁币种和数量
     *
     * @param txHash
     * @return
     */
    @Override
    public Map<String, Object> getBurnAsset(String txHash) {
        Map<String, Object> resultMap = new HashMap<>();
        return resultMap;
    }

    /**
     * 获取铸币币种总量
     *
     * @param policyId
     * @param assetName
     * @return
     */
    @Override
    public String getAsset(String policyId, String assetName) {
        return "";
    }

}
