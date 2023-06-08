package com.feidian.ChromosView.service;

import com.feidian.ChromosView.domain.Chromosome;
import com.feidian.ChromosView.domain.Cultivar;
import sun.util.resources.cldr.ii.LocaleNames_ii;

import java.util.List;

public interface CultivarService {
    public List<Cultivar> findAllCultivar();
    public List<Cultivar> findCultivarBySpeciesID(int id);
    public List<Chromosome> findAllChromosome();
    public List<Chromosome> findCSByID(int id);
}
