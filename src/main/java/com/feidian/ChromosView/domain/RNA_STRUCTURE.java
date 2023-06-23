package com.feidian.ChromosView.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RNA_STRUCTURE {
    int mRNA_ID;
    int EXON_ID;
    long START_POINT;
    long END_POINT;
}
