package com.feidian.ChromosView.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompartmentPointMB {
    int ID;
    int CS_ID;
    double START_POINT;
    double END_POINT;
    double VALUE;

    public CompartmentPointMB(CompartmentPoint compartmentPoint) {
        this.ID = compartmentPoint.getID();
        this.CS_ID = compartmentPoint.getCS_ID();
        this.VALUE = compartmentPoint.getVALUE();
        this.END_POINT = compartmentPoint.getEND_POINT() / 1000000.0;
        this.START_POINT = compartmentPoint.getSTART_POINT() / 1000000.0;
    }
    //TODO:修复使用染色体名称而不是染色体id
    public String toString(){
        String temp=ID+"\t"+CS_ID+"\t"+START_POINT+"\t"+END_POINT+"\t"+VALUE+"\n";
        return temp;
    }


}
