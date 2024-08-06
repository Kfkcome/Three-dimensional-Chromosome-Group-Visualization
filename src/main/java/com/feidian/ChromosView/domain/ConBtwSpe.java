package com.feidian.ChromosView.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConBtwSpe {
    String CS_NAME1, CS_NAME2;
    Integer s1, s2, e1, e2;
    String GENE_NAME1, GENE_NAME2;
    String PROPERTY1, PROPERTY2;
}
