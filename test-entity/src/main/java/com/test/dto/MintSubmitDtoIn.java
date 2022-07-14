package com.test.dto;

import com.test.validate.enumValidate.EnumValidation;
import com.test.enums.BlockChainType;
import com.test.validate.group.MyValidateGroup;
import com.test.validate.stringArrayValidate.StringArrayValidation;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.Instant;

@Data
@ToString(callSuper = true)
public class MintSubmitDtoIn {

    /**
     * 链名称
     */
    @ApiModelProperty("链名称")
    @NotNull(message = "链名称不能为空")
    @EnumValidation(clazz = BlockChainType.class, method = "getBlockChainName", message = "不支持的链")
    public String blockChain;

    /**
     * 资金账户
     */
    @ApiModelProperty("资金账户")
    @NotNull(message = "资金账户不能为空")
    public String source;

    /**
     * 签名信息
     */
    @ApiModelProperty("签名信息")
    @NotNull(message = "签名信息不能为空")
    public String signInfo;

    /**
     * 客户标识
     */
    @ApiModelProperty("客户标识")
    @NotNull(message = "客户标识不能为空")
    public String clientId;

    @ApiModelProperty("time_to_live")
    @NotNull(message = "time_to_live is null")
    @Future(message = "invalid time_to_live")
    private Instant time_to_live;

    @ApiModelProperty("parties")
    @NotNull(message = "parties is null")
    @Size(min = 2, max = 2, message = "parties length invalid")
    private Integer[] parties;

    /**
     * public_key
     */
    @ApiModelProperty("public_key")
    @NotNull(message = "public_key is null")
    @Size(min = 2, message = "public_key length invalid")
    @StringArrayValidation(groups = MyValidateGroup.class, message = "invalid public_key")
    private String[] public_key;

}
