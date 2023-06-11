package com.feidian.ChromosView.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoopPoint {
    int IC_ID;
    int CS_ID;
    long START_POINT1;
    long END_POINT1;
    long START_POINT2;
    long END_POINT2;
    int IC_NUM;
    double FDR_VALUE;
}
