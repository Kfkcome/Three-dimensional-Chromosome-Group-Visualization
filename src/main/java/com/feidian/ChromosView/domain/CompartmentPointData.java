package com.feidian.ChromosView.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompartmentPointData {
    //<span style='color:red; font-family: arial; font-size: 12pt;'>Compartment.bw
    // </span><span style='font-family: arial; font-size: 12pt;'>
    // <br>4,000,000-4,040,000<br>
    // bin: 100<br>
    // value: 0.007</span>

    //<span style='color:red; font-family: arial; font-size: 12pt;'>Compartment.bw</span>
    // <span style='font-family: arial; font-size: 12pt;'>
    // <br>bin: 100,000</span>

    String annotationName;
    String location;
    String bin;
    String value;

    public CompartmentPointData(String data) {
        System.out.println(data);
        String[] split = data.split("<br>");
        if (split.length > 2) {
            annotationName = split[0].split("</span>")[0].split(">")[1];
            location = split[1];
            bin = split[2].split(":")[1].trim();
            value = split[3].split(":")[1].split("</span>")[0].trim();
        } else {
            annotationName = split[0].split("</span>")[0].split(">")[1];
            bin = split[1].split("</span>")[0].split(":")[1].trim();
            location = null;
            value = null;
        }
    }

}
