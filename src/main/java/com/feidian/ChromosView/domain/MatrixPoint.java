package com.feidian.ChromosView.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class MatrixPoint implements Serializable {
    long binX;
    long binY;
    long genomeX;
    long genomeY;
    float counts;
}
