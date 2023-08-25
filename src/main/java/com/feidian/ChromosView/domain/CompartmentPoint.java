package com.feidian.ChromosView.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompartmentPoint {
    int ID;
    int CS_ID;
    long START_POINT;
    long END_POINT;
    double VALUE;

    public String toString() {
        return START_POINT + "\t" + END_POINT + "\t" + VALUE + "\n";
    }
}
