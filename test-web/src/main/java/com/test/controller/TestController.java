package com.test.controller;

import com.test.dto.MintSubmitDtoIn;
import com.test.exception.BusinessException;
import com.test.response.Response;
import com.test.response.ResponseFactory;
import com.test.service.ITestService;
import com.test.validate.group.SortValidateGroup;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;

/**
 * 功能描述: 服务控制类
 *
 * @author hua_chao
 * @create 2022/3/18 下午3:58
 * @since 1.0.0
 */
@Api(tags = {"接口"})
@RestController
@RequestMapping("/control")
@Slf4j
@Validated
public class TestController {

    @Resource
    private ITestService iTestService;

    /**
     * 铸币提交，校验是否超发
     *
     * @param request
     * @return
     */
    @PostMapping("/mint/submit")
    @Operation(summary = "铸币提交")
    public Response mintSubmit(@Validated(value = SortValidateGroup.class) @RequestBody MintSubmitDtoIn request) {
        log.info("[风控-铸币提交]入参: {}", request);
        Response response = iTestService.mintSubmit(request);
        log.info("[风控-铸币提交]end {}", response);
        return response;
    }

    /**
     * 铸币状态查询
     *
     * @param txId
     * @return
     */
    @GetMapping("/mint/query")
    public Response mintQuery(@RequestParam(value = "txId", required = false) @NotNull(message = "txId不能为空") String txId) {
        log.info("[风控-铸币查询]入参: {}", txId);
        try {
            Response response = iTestService.mintQuery(txId);
            log.info("[风控-铸币查询]end {}", response);
            return response;
        } catch (BusinessException e) {
            Response response = ResponseFactory.getError(e);
            log.info("[风控-铸币查询]end {}", response);
            return response;
        }
    }

    /**
     * 铸币状态查询
     *
     * @param txId
     * @return
     */
    @GetMapping("/mint/{txId}")
    public Response mintQuery1(@PathVariable(name = "txId", required = false) String txId) {
        log.info("[风控-铸币查询]入参: {}", txId);
        try {
            Response response = iTestService.mintQuery(txId);
            log.info("[风控-铸币查询]end {}", response);
            return response;
        } catch (BusinessException e) {
            Response response = ResponseFactory.getError(e);
            log.info("[风控-铸币查询]end {}", response);
            return response;
        }
    }

}
