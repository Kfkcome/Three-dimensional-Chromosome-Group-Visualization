package com.feidian.ChromosView.utils;

import com.feidian.ChromosView.domain.*;

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

    public static List<RNA_T> convertRNA(List<RNA> rnaList) {
        List<RNA_T> rnaTs = new ArrayList<>();
        for (RNA rna : rnaList) {
            rnaTs.add(new RNA_T(rna));
        }
        return rnaTs;
    }
}
