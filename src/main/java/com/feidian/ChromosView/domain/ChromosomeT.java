package com.feidian.ChromosView.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChromosomeT {
    int CS_ID;
    String CS_NAME;
    long CS_LENGTH;
    int CULTIVAR_ID;
}
