package com.feidian.ChromosView.service.impl;

import com.feidian.ChromosView.domain.Chromosome;
import com.feidian.ChromosView.domain.Cultivar;
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
    public List<Cultivar> findAllCultivar() {
        return cultivarMapper.findAll();
    }

    @Override
    public List<Cultivar> findCultivarBySpeciesID(int id) {
        List<Cultivar> bclaBySpeciesID = cultivarMapper.findBCLABySpeciesID(id);
        return bclaBySpeciesID;
    }

    @Override
    public List<Chromosome> findAllChromosome() {
        return chromosomeMapper.findAll();
    }

    @Override
    public List<Chromosome> findCSByID(int id) {
        List<Chromosome> byCultivarID = chromosomeMapper.findByCultivarID(id);
        return byCultivarID;
    }
}
