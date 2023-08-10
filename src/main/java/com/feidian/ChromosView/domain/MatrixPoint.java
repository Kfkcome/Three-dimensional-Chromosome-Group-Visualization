package com.feidian.ChromosView.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class MatrixPoint implements Serializable {
    double binX;
    double binY;
    long genomeX;
    long genomeY;
    float counts;
}
