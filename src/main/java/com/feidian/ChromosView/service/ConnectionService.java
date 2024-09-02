package com.feidian.ChromosView.service;

import com.feidian.ChromosView.domain.CpData;
import com.feidian.ChromosView.domain.LoopData;
import com.feidian.ChromosView.domain.TadData;

import java.util.List;
import java.util.Map;

public interface ConnectionService {
    List<String> getGenusName();

    List<String> getSpeciesName(String genus);

    List<String> getCultivarName(String genus, String species);

    List<String> getChromosomeName(String species, String cultivar);

    Map<String, Object> getConnectionALL(String cs_name1, String cs_name2, String target1, String target2);

    Map<String, Object> getConnectionByRange(String cs_name1, String target1, String target2
            , Integer s1, Integer e1);

    List<TadData> getTadALL(String species, String cultivar, String chromosome, String software);

    List<TadData> getTadByRange(String species, String cultivar, String chromosome, Integer s, Integer e, String software);

    List<LoopData> getLoopALL(String species, String cultivar, String chromosome, String software);

    List<LoopData> getLoopByRange(String species, String cultivar, String chromosome, Integer s, Integer e, String software);

    List<CpData> getCompartmentALL(String species, String cultivar, String chromosome);

    List<CpData> getCompartmentByRange(String species, String cultivar, String chromosome, Integer s, Integer e);
}
