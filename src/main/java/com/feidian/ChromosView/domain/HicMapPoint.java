package com.feidian.ChromosView.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HicMapPoint {
    String chromosome;
    String position;
    String observerValue;
    String expectedValue;
    String OE;

    public HicMapPoint(String point) {
        //<html><span style='color:#0000FF; font-family: arial; font-size: 12pt; '>
        // SoyC02.Chr01:80,001-120,000
        // </span><br><span style='color:#009900; font-family: arial; font-size: 12pt; '>
        // SoyC02.Chr01:80,001-120,000
        // </span><span style='font-family: arial; font-size: 12pt;'><br><span style='font-family: arial; font-size: 12pt;'>
        // observed value (O) = 2,776</span><br><span style='font-family: arial; font-size: 12pt;'>
        // expected value (E) = 2,993.992</span><br><span style='font-family: arial; font-size: 12pt;'>
        // O/E            = 0.927</span><br></html>

        //携带注释的点的信息
        //<html><span style='color:#0000FF; font-family: arial; font-size: 12pt; '>
        // SoyC02.Chr04:80,001-120,000
        // </span><br><span style='color:#009900; font-family: arial; font-size: 12pt; '>
        // SoyC02.Chr04:80,001-120,000
        // </span><span style='font-family: arial; font-size: 12pt;'><br><span style='font-family: arial; font-size: 12pt;'>
        // observed value (O) = 2,893</span><br><span style='font-family: arial; font-size: 12pt;'>
        // expected value (E) = 3,122.613</span><br><span style='font-family: arial; font-size: 12pt;'>
        // O/E            = 0.926</span><span style='color:#0000FF; font-family: arial; font-size: 12pt; '><br><br><span style='color:red; font-family: arial; font-size: 12pt;'>gene.bed.gz</span></span><br></html>

        String[] split = point.split("<html><span style='color:#0000FF; font-family: arial; font-size: 12pt; '>");
        String[] split1 = split[1].split("</span><br><span style='color:#009900; font-family: arial; font-size: 12pt; '>");
        String[] split2 = split1[0].split(":");
        chromosome = split2[0];
        position = split2[1];
        String[] split3 = split1[1].split("</span><br><span style='font-family: arial; font-size: 12pt;'>");
        observerValue = split3[0].split("=")[split3[0].split("=").length - 1];
        expectedValue = split3[1].split("=")[1];
        OE = split3[2].split("=")[1].split("</span>")[0];
    }
}
