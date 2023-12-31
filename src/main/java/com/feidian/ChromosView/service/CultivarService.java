package com.feidian.ChromosView.service;

import com.feidian.ChromosView.domain.ChromosomeT;
import com.feidian.ChromosView.domain.Software;

import java.util.List;
import java.util.Map;

public interface CultivarService {
    public List<String> findAllCultivar();

    public List<String> findCultivarBySpeciesName(String speciesName);

    public List<ChromosomeT> findAllChromosome();

    public List<ChromosomeT> findCSByCultivar(String SpeciesName, String CultivarName);

    public List<String> findTissueByID(String SpeciesName, String CultivarName);

    public List<Software> findAllSoftware();

    public Map<String, List<String>> findTheSoftware(String SpeciesName, String CultivarName, String TissueName);
}
