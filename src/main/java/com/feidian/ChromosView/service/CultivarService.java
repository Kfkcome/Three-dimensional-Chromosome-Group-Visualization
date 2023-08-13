package com.feidian.ChromosView.service;

import com.feidian.ChromosView.domain.ChromosomeT;
import com.feidian.ChromosView.domain.Cultivar;
import com.feidian.ChromosView.domain.Software;
import com.feidian.ChromosView.domain.Tissue;

import java.util.List;

public interface CultivarService {
    public List<Cultivar> findAllCultivar();
    public List<Cultivar> findCultivarBySpeciesID(int id);
    public List<ChromosomeT> findAllChromosome();
    public List<ChromosomeT> findCSByID(int id);
    public List<Tissue> findTissueByID(int CultivarID);
    public List<Software>findAllSoftware();
}
