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
    Double START_POINT;
    Double END_POINT;
    int IC_NUM;
    double FDR_VALUE;

    public LoopPointMB(LoopPoint loopPoint) {
        this.END_POINT = (loopPoint.END_POINT1 + loopPoint.END_POINT2) / 2.0 / 1000000.0;
        this.START_POINT = (loopPoint.START_POINT1 + loopPoint.getSTART_POINT2()) / 2.0 / 1000000.0;
        this.IC_ID = loopPoint.getCS_ID();
        this.IC_NUM = loopPoint.getIC_NUM();
        this.CS_ID = loopPoint.CS_ID;
        this.FDR_VALUE = loopPoint.getFDR_VALUE();
    }

    public String toString() {
        String temp = IC_ID + "\t" + CS_ID + "\t" + START_POINT + "\t" + END_POINT + "\t" + IC_NUM + "\t" + FDR_VALUE + "\n";
        return temp;
    }

}
