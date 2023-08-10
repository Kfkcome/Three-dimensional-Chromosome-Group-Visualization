package com.feidian.ChromosView.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RNA_STRUCTURE_T {
    String mRna_Name;
    int EXON_ID;
    double START_POINT;
    double END_POINT;

    public RNA_STRUCTURE_T(String mRna_Name, RNA_STRUCTURE rnaStructure) {
        this.mRna_Name = mRna_Name;
        this.EXON_ID = rnaStructure.EXON_ID;
        this.START_POINT = rnaStructure.getSTART_POINT() / 1000000.0;
        this.END_POINT = rnaStructure.getEND_POINT() / 1000000.0;
    }
}
