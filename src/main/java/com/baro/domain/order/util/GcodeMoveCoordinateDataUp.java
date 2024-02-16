package com.baro.domain.order.util;

import lombok.Getter;

@Getter
public enum GcodeMoveCoordinateDataUp {
    FIRST(0, 1100 , 900),
    SECOND(800 ,1100 , 900),
    THIRD(1600 , 1100, 900),
    FORE(2350 , 1100, 900);

    private final int x;
    private final int y;
    private final int z;
    GcodeMoveCoordinateDataUp(int x , int y , int z){
        this.x = x;
        this.y =y;
        this.z = z;
    }

}
