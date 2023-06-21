package com.feidian.ChromosView.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UUID_matrixPoints {
    List<MatrixPoint> matrixPoints;
    Integer page;
    Integer pageAll;
    String uuid;
}
