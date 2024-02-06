package com.baro.domain.order.util;

import lombok.Getter;

@Getter
public enum GcodeMoveSpeedData {
    SLOW(10000),
    MEDIUM(15000),
    FAST(20000);

    private final int f;


    GcodeMoveSpeedData(int f){
        this.f = f;
    }
}
