package com.feidian.ChromosView.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cultivar {
    int CULTIVAR_ID;
    String CULTIVAR_NAME;
    int SPECIES_ID;
    int CS_NUM;
}
