package com.feidian.ChromosView.service.impl;

import com.feidian.ChromosView.domain.*;
import com.feidian.ChromosView.mapper.ChromosomeMapper;
import com.feidian.ChromosView.mapper.CultivarMapper;
import com.feidian.ChromosView.service.CultivarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
    public List<Cultivar> findAllCultivar() {
        return cultivarMapper.findAll();
    }

    @Override
    public List<Cultivar> findCultivarBySpeciesID(int id) {
        List<Cultivar> bclaBySpeciesID = cultivarMapper.findBCLABySpeciesID(id);
        return bclaBySpeciesID;
    }

    @Override
    public List<ChromosomeT> findAllChromosome() {
        return chromosomeMapper.findAll();
    }

    @Override
    public List<ChromosomeT> findCSByID(int id) {
        List<ChromosomeT> byCultivarID = chromosomeMapper.findByCultivarID(id);
        return byCultivarID;
    }

    @Override
    public List<Tissue> findTissueByID(int CultivarID) {
        List<Tissue_Cultivar> tissueByCultivarID = cultivarMapper.findTissueByCultivarID(CultivarID);
        ArrayList<Tissue> tissues = new ArrayList<>();
        for (Tissue_Cultivar tissueCultivar : tissueByCultivarID) {
            tissues.add(cultivarMapper.findTissueByID(tissueCultivar.getTISSUE_ID()));
        }
        return tissues;
    }

    @Override
    public List<Software> findAllSoftware() {
        return cultivarMapper.findAllSoftware();
    }
}
