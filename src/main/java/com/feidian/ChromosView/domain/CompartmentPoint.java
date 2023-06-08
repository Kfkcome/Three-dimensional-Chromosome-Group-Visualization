package com.feidian.ChromosView.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompartmentPoint {
    int CS_ID;
    long START_POINT;
    int END_POINT;
    int VALUE;
}
