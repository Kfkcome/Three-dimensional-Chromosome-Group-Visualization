package com.feidian.ChromosView.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class MatrixPoint {
    int binX;
    int binY;
    int genomeX;
    int genomeY;
    float counts;
}
