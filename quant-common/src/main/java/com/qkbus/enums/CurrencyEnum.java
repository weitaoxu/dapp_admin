package com.qkbus.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CurrencyEnum {


    /* USDT */
    USDT("USDT", "USDT"),


    ;

    private final String currency;

    private final String currencyName;


}
