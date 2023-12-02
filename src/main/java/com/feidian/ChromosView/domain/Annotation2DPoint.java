package com.feidian.ChromosView.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class Annotation2DPoint {
    ArrayList<Feature2DPoint> feature2DPoints;

    public Annotation2DPoint(List<String> data) {
        feature2DPoints = new ArrayList<>();
        for (String datum : data) {
            String[] split = datum.split("Feature");
            if (split.length > 1) {
                feature2DPoints.add(new Feature2DPoint(datum));
            }
        }
    }

}
