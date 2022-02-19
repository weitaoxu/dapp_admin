package com.qkbus.enums;


import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum WalletLogEnum {

    LOG_TYPE_RECHARGE(0, "充值"),
    LOG_TYPE_WITHDRAW(1, "提现"),
    LOG_TYPE_TRANS(2, "互转"),
    LOG_TYPE_INVITE(3, "燃料"),
    LOG_TYPE_CDKEY(4, "购买激活码"),
    LOG_TYPE_CREDIT(5, "购买信誉金"),
    HAND_UPDATE(6, "手动修改资产"),


    ;
    @ApiModelProperty("类型")
    private final Integer logType;

    @ApiModelProperty("描述")
    private final String comment;


    // 普通方法
    public static String getComment(Integer logType) {
        for (WalletLogEnum logEnum : WalletLogEnum.values()) {
            if (logEnum.logType.equals(logType)) {
                return logEnum.comment;
            }
        }
        return "";
    }

}
