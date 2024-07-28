package com.feidian.ChromosView.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExcelHiCMatrix {
    String Specie;
    String Cultivar;
    String Tissue;
    String Matrix_10K_HiC_Pro;
    String Bed_10K_HiC_Pro;
    String Matrix_40K_HiC_Pro;
    String Bed_40K_HiC_Pro;
    String Matrix_100K_HiC_Pro;
    String Bed_100K_HiC_Pro;
    String AllValidPairs_Files;
    String Matrix_hic_format;
}
