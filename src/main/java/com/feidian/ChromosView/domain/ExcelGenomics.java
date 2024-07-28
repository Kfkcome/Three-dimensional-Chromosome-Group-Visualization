package com.feidian.ChromosView.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExcelGenomics {
    String Specie;
    String Cultivar;
    String Chromosomes_Scaffolds_FASTA_Files;
    String Predicted_Genes_GFF3_Files;
    String Predicted_coding_sequences_FASTA_Files;
    String Predicted_protein_sequences_FASTA_Files;
    String REF;
}
