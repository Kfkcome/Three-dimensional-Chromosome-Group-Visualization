package com.feidian.ChromosView.service.impl;

import com.feidian.ChromosView.domain.ChromosomeT;
import com.feidian.ChromosView.domain.Software;
import com.feidian.ChromosView.mapper.ChromosomeMapper;
import com.feidian.ChromosView.mapper.CultivarMapper;
import com.feidian.ChromosView.service.CultivarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
}
