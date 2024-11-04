package com.feidian.ChromosView.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Feature2DPoint {
    //<span style='color:red; font-family: arial; font-size: 12pt;'>Feature</span><br>
    // <span style='font-family: arial; font-size: 12pt;color:#0000FF;'>Ghir_A01:3,760,001-6,760,000</span><br>
    // <span style='font-family: arial; font-size: 12pt;color:#009900;'>Ghir_A01:3,760,001-6,760,000</span><br>
    // <span style='font-family: arial; font-size: 12pt;'>loSign = <b>0.69</b></span><br>
    // <span style='font-family: arial; font-size: 12pt;'>lVarScore = <b>0.09</b></span><br>
    // <span style='font-family: arial; font-size: 12pt;'>score = <b>1.08</b></span><br>
    // <span style='font-family: arial; font-size: 12pt;'>upSign = <b>0.61</b></span><br>
    // <span style='font-family: arial; font-size: 12pt;'>uVarScore = <b>0.09</b></span><br>
//<span style='color:red; font-family: arial; font-size: 12pt;'>Feature</span><br>
// <span style='font-family: arial; font-size: 12pt;color:#0000FF;'>Gm_C01_Chr1:45,870,001-45,880,000</span><br>
// <span style='font-family: arial; font-size: 12pt;color:#009900;'>Gm_C01_Chr1:45,330,001-45,340,000</span><br>
// <span style='font-family: arial; font-size: 12pt;'>pvalue = <b>0.03</b></span><br>

    //<span style='color:red; font-family: arial; font-size: 12pt;'>Feature</span><br>
// <span style='font-family: arial; font-size: 12pt;color:#0000FF;'>Gm_C01_Chr1:14,800,001-15,520,000</span><br>
// <span style='font-family: arial; font-size: 12pt;color:#009900;'>Gm_C01_Chr1:14,800,001-15,520,000</span>
    String name;
    String range1;
    String range2;
    String pValue;

    Feature2DPoint(String data) {
        String[] split = data.split("</span><br>");
        name = split[0].split(">")[1];
        range1 = split[1].split(">")[1];
//        range2 = split[2].split(">")[1];
        if (split.length <= 3) {
            range2 = split[2].split(">")[1].split("</span")[0];
        }
        if (split.length > 3) {
            range2 = split[2].split(">")[1];
            pValue = split[3].split("<b>")[1].split("</b>")[0];
        }
    }
}
