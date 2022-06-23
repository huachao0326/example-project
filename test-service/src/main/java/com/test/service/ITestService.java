package com.test.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.test.dto.MintSubmitDtoIn;
import com.test.entity.MintRequest;
import com.test.response.Response;

/**
 * 功能描述:信托风控 服务接口
 *
 * @author hua_chao
 * @create 2022/3/18 下午3:58
 * @since 1.0.0
 */
public interface ITestService extends IService<MintRequest> {

    /**
     * 铸币提交，校验是否超发
     *
     * @param request
     * @return
     */
    Response mintSubmit(MintSubmitDtoIn request);

    /**
     * 铸币查询，查询交易是否成功
     *
     * @param txId
     * @return
     */
    Response mintQuery(String txId);

}
