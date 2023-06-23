package com.feidian.ChromosView.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RNA {
    int CS_ID;
    int mRNA_ID;
    String mRNA_NAME;
    long START_POINT;
    long END_POINT;
    int DIRECTION;//方向
}
