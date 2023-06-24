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
    long START_POINT;
    long END_POINT;

    public RNA_STRUCTURE_T(String mRna_Name, RNA_STRUCTURE rnaStructure) {
        this.mRna_Name = mRna_Name;
        this.EXON_ID = rnaStructure.EXON_ID;
        this.START_POINT = rnaStructure.getSTART_POINT();
        this.END_POINT = rnaStructure.getEND_POINT();
    }
}
