package com.feidian.ChromosView.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RNA_T {
    int CS_ID;
    int mRNA_ID;
    String mRNA_NAME;
    double START_POINT;
    double END_POINT;
    int DIRECTION;//方向

    public RNA_T(RNA rna) {
        this.CS_ID = rna.getCS_ID();
        this.DIRECTION = rna.getDIRECTION();
        this.mRNA_NAME = rna.getMRNA_NAME();
        this.mRNA_ID = rna.getMRNA_ID();
        this.START_POINT = rna.getSTART_POINT() / 1000000.0;
        this.END_POINT = rna.getEND_POINT() / 1000000.0;
    }
}
