package com.feidian.ChromosView.utils;

import com.feidian.ChromosView.domain.CompartmentPoint;
import com.feidian.ChromosView.domain.CompartmentPointMB;
import com.feidian.ChromosView.domain.LoopPoint;
import com.feidian.ChromosView.domain.LoopPointMB;

import java.util.ArrayList;
import java.util.List;

public class UnitConversion {
    public static ArrayList<CompartmentPointMB> convert(ArrayList<CompartmentPoint> arrayList) {
        ArrayList<CompartmentPointMB> mbArrayList = new ArrayList<>();
        for (CompartmentPoint compartmentPoint : arrayList) {
            CompartmentPointMB compartmentPointMB = new CompartmentPointMB(compartmentPoint);
            mbArrayList.add(compartmentPointMB);
        }
        return mbArrayList;
    }

    public static ArrayList<LoopPointMB> convertLoop(List<LoopPoint> loopPointArrayList) {
        ArrayList<LoopPointMB> loopPointMBS = new ArrayList<>();
        for (LoopPoint loopPoint : loopPointArrayList) {
            LoopPointMB loopPointMB = new LoopPointMB(loopPoint);
            loopPointMBS.add(loopPointMB);
        }
        return loopPointMBS;
    }
}
