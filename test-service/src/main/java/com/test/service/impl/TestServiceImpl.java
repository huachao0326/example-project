package com.test.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.test.dto.MintSubmitDtoIn;
import com.test.entity.MintRequest;
import com.test.enums.TradeStatus;
import com.test.exception.BusinessException;
import com.test.mapper.MintRequestMapper;
import com.test.response.ErrorCode;
import com.test.response.Response;
import com.test.response.ResponseFactory;
import com.test.service.AbstractService;
import com.test.service.ITestService;
import com.test.util.UUIDGenerator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 功能描述:信托风控 服务实现类
 *
 * @author hua_chao
 * @create 2022/3/18 下午3:58
 * @since 1.0.0
 */
@Service
@Slf4j
public class TestServiceImpl extends ServiceImpl<MintRequestMapper, MintRequest> implements ITestService {

    @Resource
    MintRequestMapper mintRequestMapper;

    @Resource
    ApplicationContext applicationContext;

    /**
     * 铸币提交，校验是否超发
     *
     * @param request
     * @return
     */
    @Override
    public Response mintSubmit(MintSubmitDtoIn request) {
        //反序列化交易信息，获取币种、金额、policyId
        AbstractService abstractService = (AbstractService) applicationContext.getBean(request.blockChain + "Service");
        Map<String, Object> assetMap = abstractService.deserialize(request.signInfo);
        //登记铸币交易信息
        String txId = UUIDGenerator.get32UUID();
        MintRequest mintRequest = new MintRequest();
        BeanUtils.copyProperties(request, mintRequest);
        mintRequest.amount = (BigDecimal) assetMap.get("amount");
        mintRequest.currency = (String) assetMap.get("currency");
        mintRequest.policyId = (String) assetMap.get("policyId");
        mintRequest.txId = txId;
        mintRequest.txStatus = TradeStatus.TX_PENDING.getCode();
        mintRequest.createTime = mintRequest.updateTime = new Date();
        mintRequest.version = 1l;
        mintRequestMapper.insert(mintRequest);
        Map<String, String> result = new HashMap<>();
        result.put("txId", mintRequest.txId);
        return ResponseFactory.getSuccessData(result);
    }

    /**
     * 铸币查询，查询交易是否成功
     *
     * @param txId
     * @return
     */
    @Override
    public Response mintQuery(String txId) throws BusinessException {
        LambdaQueryWrapper<MintRequest> mintRequestLambdaQueryWrapper = new LambdaQueryWrapper<>();
        mintRequestLambdaQueryWrapper.eq(MintRequest::getTxId, txId);
        MintRequest mintRequest = this.getOne(mintRequestLambdaQueryWrapper);
        Map<String, Object> resultMap = new HashMap<>();
        if (null == mintRequest) {
            throw new BusinessException(ErrorCode.TX_NOT_FOUND.getErrorCode(), ErrorCode.TX_NOT_FOUND.getErrorMsg());
        }
        resultMap.put("txHash", StringUtils.isNotEmpty(mintRequest.txHash) ? mintRequest.txHash : null);
        resultMap.put("isOverIssue", TradeStatus.TX_SUCCESS.getCode() == mintRequest.txStatus ? Boolean.FALSE : (TradeStatus.TX_ERROR.getCode() == mintRequest.txStatus ? Boolean.TRUE : null));
        resultMap.put("txStatus", TradeStatus.getNameByCode(mintRequest.txStatus));
        return ResponseFactory.getSuccessData(resultMap);
    }

}
