package com.baro.domain.order.util;

import lombok.Getter;

@Getter
public enum GcodeMoveCoordinateData {
    FIRST(400, 1000 , 900),
    SECOND(1200 ,0 , 900),
    THIRD(2000 , 1000, 900),
    FORE(2500 , 0, 900);

    private final int x;
    private final int y;
    private final int z;
    GcodeMoveCoordinateData(int x , int y , int z){
        this.x = x;
        this.y =y;
        this.z = z;
    }
}
