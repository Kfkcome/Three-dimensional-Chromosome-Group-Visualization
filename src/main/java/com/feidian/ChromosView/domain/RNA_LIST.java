package com.feidian.ChromosView.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RNA_LIST {
    List<List<RNA_T>> rnaList;
    List<List<RNA_STRUCTURE_T>> rnaStructureTs;
}
