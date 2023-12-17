package com.feidian.ChromosView.service.impl;

import com.feidian.ChromosView.mapper.SpeciesMapper;
import com.feidian.ChromosView.service.SpeciesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpeciesServiceImpl implements SpeciesService {
    private final SpeciesMapper speciesMapper;

    @Autowired
    SpeciesServiceImpl(SpeciesMapper speciesMapper) {
        this.speciesMapper = speciesMapper;
    }

    @Override
    public List<String> findALLSpecies() {
        List<String> all = speciesMapper.findAll();
        return all;
    }
}
