package com.feidian.ChromosView.utils;

import com.feidian.ChromosView.domain.CompartmentPoint;
import com.feidian.ChromosView.domain.CompartmentPointMB;

import java.util.ArrayList;

public class UnitConversion {
    public static ArrayList<CompartmentPointMB>convert(ArrayList<CompartmentPoint> arrayList){
        ArrayList<CompartmentPointMB> mbArrayList=new ArrayList<>();
        for (CompartmentPoint compartmentPoint : arrayList) {
            CompartmentPointMB compartmentPointMB=new CompartmentPointMB(compartmentPoint);
            mbArrayList.add(compartmentPointMB);
        }
        return mbArrayList;
    }
}
