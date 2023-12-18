package com.feidian.ChromosView.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SoftwareOfAnnotation2D {
    String CULTIVAR_ID;
    String Samples;
    Boolean TAD_Arrowhead;
    Boolean TAD_ClusterTAD;
    Boolean TAD_Cworld;
    Boolean TAD_deDoc;
    Boolean TAD_domaincaller;
    Boolean TAD_HiCExplorer;
    Boolean TAD_HiCseg;
    Boolean TAD_ICFinder;
    Boolean TAD_MSTD;
    Boolean TAD_OnTAD;
    Boolean TAD_rGMAP;
    Boolean TAD_Spectral;
    Boolean TAD_TADLib;
    Boolean TAD_TopDom;
    Boolean Loop_FitHiC;
    Boolean Loop_HiCCUPS;
    Boolean Loop_HiCExplorer;

    public List<Boolean> getBinary() {
        ArrayList<Boolean> booleans = new ArrayList<>();
        booleans.add(TAD_Arrowhead);
        booleans.add(TAD_ClusterTAD);
        booleans.add(TAD_Cworld);
        booleans.add(TAD_deDoc);
        booleans.add(TAD_domaincaller);
        booleans.add(TAD_HiCExplorer);
        booleans.add(TAD_HiCseg);
        booleans.add(TAD_ICFinder);
        booleans.add(TAD_MSTD);
        booleans.add(TAD_OnTAD);
        booleans.add(TAD_rGMAP);
        booleans.add(TAD_Spectral);
        booleans.add(TAD_TADLib);
        booleans.add(TAD_TopDom);
        booleans.add(Loop_FitHiC);
        booleans.add(Loop_HiCCUPS);
        booleans.add(Loop_HiCExplorer);
        return booleans;
    }
}
