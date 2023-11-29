package com.feidian.ChromosView.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GeneStructPoint {
    String annotationName;
    String geneName;

    public GeneStructPoint(String data) {
        String[] split = data.split("</span>");
        annotationName = split[0].split(">")[1];
        if (split.length > 1) {
            geneName = split[1].split("<br>")[1];
        } else geneName = null;
    }
    // <span style='color:red; font-family: arial; font-size: 12pt;'>gene.bed.gz
    // </span><span style='font-family: arial; font-size: 12pt;'>
    // <br>Ghir_A07G003530</span>

    //<span style='color:red; font-family: arial; font-size: 12pt;'>gene.bed.gz</span>
}
