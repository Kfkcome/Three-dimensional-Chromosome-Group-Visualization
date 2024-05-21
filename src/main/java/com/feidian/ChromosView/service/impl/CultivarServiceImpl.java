package com.feidian.ChromosView.service.impl;

import com.feidian.ChromosView.domain.ChromosomeT;
import com.feidian.ChromosView.domain.Software;
import com.feidian.ChromosView.domain.SoftwareOfAnnotation2D;
import com.feidian.ChromosView.mapper.ChromosomeMapper;
import com.feidian.ChromosView.mapper.CultivarMapper;
import com.feidian.ChromosView.service.CultivarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CultivarServiceImpl implements CultivarService {
    private final CultivarMapper cultivarMapper;
    private final ChromosomeMapper chromosomeMapper;

    @Autowired
    public CultivarServiceImpl(CultivarMapper cultivarMapper, ChromosomeMapper chromosomeMapper) {
        this.cultivarMapper = cultivarMapper;
        this.chromosomeMapper = chromosomeMapper;
    }

    @Override
    public List<String> findAllCultivar() {
        return cultivarMapper.findAll();
    }

    @Override
    public List<String> findCultivarBySpeciesName(String speciesName) {
        List<String> bclaBySpeciesID = cultivarMapper.findBCLABySpeciesID(speciesName);
        return bclaBySpeciesID;
    }

    @Override
    public List<ChromosomeT> findAllChromosome() {
        return chromosomeMapper.findAll();
    }

    @Override
    public List<ChromosomeT> findCSByCultivar(String SpeciesName, String CultivarName) {

        Integer minIdByCultivar = cultivarMapper.findMinIdByCultivar(SpeciesName, CultivarName);
        if (minIdByCultivar == null) {
            //TODO:处理没有找到的错误
            return null;
        }
        List<ChromosomeT> byCultivarID = chromosomeMapper.findByCultivarID(minIdByCultivar);
        return byCultivarID;
    }

    @Override
    public List<String> findTissueByID(String SpeciesName, String CultivarName) {
        List<String> tissue = cultivarMapper.findTissueByCultivar(SpeciesName, CultivarName);
//        ArrayList<Tissue> tissues = new ArrayList<>();
//        if (tissueByCultivarID.isEmpty()) {
//            return null;
//        }
//        for (Tissue_Cultivar tissueCultivar : tissueByCultivarID) {
//            tissues.add(cultivarMapper.findTissueByID(tissueCultivar.getTISSUE_ID()));
//        }
        return tissue;
    }

    @Override
    public List<Software> findAllSoftware() {
        return cultivarMapper.findAllSoftware();
    }

    @Override
    public Map<String, List<String>> findTheSoftware(String SpeciesName, String CultivarName, String TissueName) {
        Integer byName = cultivarMapper.findByName(SpeciesName, CultivarName, TissueName);
        if (byName == null) {
            return null;
        }
        SoftwareOfAnnotation2D theSoftware = cultivarMapper.findTheSoftware(byName);
        List<Boolean> binary = theSoftware.getBinary();
        List<String> tadAvailable = new ArrayList<>();
        List<String> loopAvailable = new ArrayList<>();
        ArrayList<String> strings = new ArrayList<>(Arrays.asList("TAD_Arrowhead",
                "TAD_ClusterTAD",
                "TAD_Cworld",
                "TAD_deDoc",
                "TAD_domaincaller",
                "TAD_HiCExplorer",
                "TAD_HiCseg",
                "TAD_ICFinder",
                "TAD_MSTD",
                "TAD_OnTAD",
                "TAD_rGMAP",
                "TAD_Spectral",
                "TAD_TADLib",
                "TAD_TopDom",
                "Loop_FitHiC",
                "Loop_HiCCUPS",
                "Loop_HiCExplorer"));
        for (int i = 0; i < binary.size(); i++) {
            if (i <= 13 && binary.get(i)) {
                tadAvailable.add(strings.get(i).split("_")[1]);
            } else loopAvailable.add(strings.get(i).split("_")[1]);
        }
        HashMap<String, List<String>> listHashMap = new HashMap<>();
        listHashMap.put("TAD", tadAvailable);
        listHashMap.put("Loop", loopAvailable);
        return listHashMap;
    }
}
