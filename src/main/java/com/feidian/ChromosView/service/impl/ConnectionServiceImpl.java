package com.feidian.ChromosView.service.impl;

import com.feidian.ChromosView.domain.ConBtwSpe;
import com.feidian.ChromosView.domain.CpData;
import com.feidian.ChromosView.domain.LoopData;
import com.feidian.ChromosView.domain.TadData;
import com.feidian.ChromosView.mapper.CpBtwCulMapper;
import com.feidian.ChromosView.service.ConnectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class ConnectionServiceImpl implements ConnectionService {
    CpBtwCulMapper cpBtwCulMapper;
    ArrayList<String> all_tissue;

    @Autowired
    ConnectionServiceImpl(CpBtwCulMapper cpBtwCulMapper) {
        this.cpBtwCulMapper = cpBtwCulMapper;
        all_tissue = new ArrayList<>(Arrays.asList(
                "Leaf",
                "Endosperm",
                "Bud",
                "Bundle-sheath",
                "Fiber-10DPA",
                "Fiber-20DPA",
                "Fiber-5DPA",
                "Ovule",
                "Placenta",
                "Pulp",
                "Root",
                "Shoot"));
    }

    @Override
    public List<String> getGenusName() {
        return cpBtwCulMapper.getGenusName();
    }

    @Override
    public List<String> getSpeciesName(String genus) {
        return cpBtwCulMapper.getSpeciesName(genus);
    }

    @Override
    public List<String> getCultivarName(String genus, String species) {
        return cpBtwCulMapper.getCultivarName(genus, species);
    }

    @Override
    public List<String> getChromosomeName(String species, String cultivar) {
        return cpBtwCulMapper.getChromosomeName(species + "_" + cultivar);
    }

    @Override
    public List<ConBtwSpe> getConnectionALL(String cs_name1, String cs_name2, String target1, String target2) {
        return cpBtwCulMapper.getConnectionALL(cs_name1, cs_name2, "`" + "con_" + target1 + "." + target2 + "`");
    }

    @Override
    public List<ConBtwSpe> getConnectionByRange(String cs_name1, String cs_name2, String target1, String target2, Integer s1, Integer e1, Integer s2, Integer e2) {
        return cpBtwCulMapper.getConnectionByRange(cs_name1, cs_name2, "`" + "con_" + target1 + "." + target2 + "`", s1, e1, s2, e2);
    }

    @Override
    public List<TadData> getTadALL(String species, String cultivar, String chromosome, String software) {
        String table_name;
        List<TadData> tadDataByChromosome = null;
        for (String s : all_tissue) {
            table_name = "`tad_" + species + "_" + cultivar + '_' + s + '_' + software + "`";
            try {
                tadDataByChromosome = cpBtwCulMapper.getTadDataByChromosome(table_name, chromosome);
                return tadDataByChromosome;

            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
        return tadDataByChromosome;
    }

    @Override
    public List<TadData> getTadByRange(String species, String cultivar, String chromosome, Integer s, Integer e, String software) {
        String table_name;
        List<TadData> tadDataByChromosome = null;
        for (String t : all_tissue) {
            table_name = "`tad_" + species + "_" + cultivar + "_" + t + '_' + software + "`";
            try {
                tadDataByChromosome = cpBtwCulMapper.getTadDataByRange(table_name, chromosome, s, e);
                return tadDataByChromosome;

            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
        return tadDataByChromosome;
    }

    @Override
    public List<LoopData> getLoopALL(String species, String cultivar, String chromosome, String software) {
        String table_name;
        List<LoopData> loopDataByChromosome = null;
        for (String t : all_tissue) {
            table_name = "`loop_" + species + "_" + cultivar + "_" + t + '_' + software + "`";
            try {
                loopDataByChromosome = cpBtwCulMapper.getLoopDataByChromosome(table_name, chromosome);
                return loopDataByChromosome;

            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
        return loopDataByChromosome;
    }

    @Override
    public List<LoopData> getLoopByRange(String species, String cultivar, String chromosome, Integer s, Integer e, String software) {
        String table_name;
        List<LoopData> loopDataByChromosome = null;
        for (String t : all_tissue) {
            table_name = "`loop_" + species + "_" + cultivar + "_" + t + '_' + software + "`";
            try {
                loopDataByChromosome = cpBtwCulMapper.getLoopDataByRange(table_name, chromosome, s, e);
                return loopDataByChromosome;

            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
        return loopDataByChromosome;
    }

    @Override
    public List<CpData> getCompartmentALL(String species, String cultivar, String chromosome) {
        String table_name;
        List<CpData> cpDataByChromosome = null;
        for (String t : all_tissue) {
            table_name = "`compartment_" + species + "_" + cultivar + "_" + t + "`";
            try {
                cpDataByChromosome = cpBtwCulMapper.getCompartmentDataByChromosome(table_name, chromosome);
                return cpDataByChromosome;

            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
        return cpDataByChromosome;
    }

    @Override
    public List<CpData> getCompartmentByRange(String species, String cultivar, String chromosome, Integer s, Integer e) {
        String table_name;
        List<CpData> cpDataByChromosome = null;
        for (String t : all_tissue) {
            table_name = "`compartment_" + species + "_" + cultivar + "_" + t + "`";
            try {
                cpDataByChromosome = cpBtwCulMapper.getCompartmentDataByRange(table_name, chromosome, s, e);
                return cpDataByChromosome;

            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
        return cpDataByChromosome;
    }
}
