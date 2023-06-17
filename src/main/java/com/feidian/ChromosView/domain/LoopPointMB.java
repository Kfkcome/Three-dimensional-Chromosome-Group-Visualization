package com.feidian.ChromosView.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoopPointMB {
    int IC_ID;
    int CS_ID;
    long START_POINT;
    long END_POINT;
    int IC_NUM;
    double FDR_VALUE;

    public LoopPointMB(LoopPoint loopPoint) {
        this.END_POINT = (loopPoint.END_POINT1 + loopPoint.END_POINT2) / 2;
        this.START_POINT = (loopPoint.START_POINT1 + loopPoint.getSTART_POINT2()) / 2;
        this.IC_ID = loopPoint.getCS_ID();
        this.IC_NUM = loopPoint.getIC_NUM();
        this.CS_ID = loopPoint.CS_ID;
        this.FDR_VALUE = loopPoint.getFDR_VALUE();
    }

}
