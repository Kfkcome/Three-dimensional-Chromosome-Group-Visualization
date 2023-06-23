package com.feidian.ChromosView.service.impl;

import com.feidian.ChromosView.domain.RNA;
import com.feidian.ChromosView.domain.RNA_STRUCTURE;
import com.feidian.ChromosView.mapper.ChromosomeMapper;
import com.feidian.ChromosView.mapper.RnaMapper;
import com.feidian.ChromosView.service.RnaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RnaServiceImpl implements RnaService {
    private final RnaMapper rnaMapper;
    private final ChromosomeMapper chromosomeMapper;

    @Autowired
    public RnaServiceImpl(RnaMapper rnaMapper, ChromosomeMapper chromosomeMapper) {
        this.rnaMapper = rnaMapper;
        this.chromosomeMapper = chromosomeMapper;
    }

    private int checkDirection(String s) {
        if (s.equals("-")) {
            return 0;
        }
        return 1;
    }

    @Override
    public Integer insertRnaFromFile(String path) {
        Map<String, Integer> saveExonNum = new HashMap<>();
        Map<String, Integer> saveRnaId = new HashMap<>();//因为一个品种中的名称唯一所以可以用来存id，避免查询花费大量实际
        ArrayList<RNA> rnaArrayList = new ArrayList<>();
        ArrayList<RNA_STRUCTURE> rnaStructures = new ArrayList<>();

        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(path));
            String s;
            int max_id = rnaMapper.getMaxID() + 1;
            while ((s = bufferedReader.readLine()) != null) {
                String[] split = s.split("\t");
                int CS_ID = chromosomeMapper.findByCultivarID_CSName(67, split[0].trim()).getCS_ID();
                long START_POINT = Long.parseLong(split[3]);
                long END_POINT = Long.parseLong(split[4]);
                if (split[2].equals("mRNA")) {
                    String mRNAName = split[8].split("=")[1].split(";")[0];
                    int direction = checkDirection(split[6]);
                    RNA rna = new RNA(CS_ID, 0, mRNAName, START_POINT, END_POINT, direction);
                    rnaArrayList.add(rna);
                    saveExonNum.put(mRNAName, 0);
                    saveRnaId.put(mRNAName, max_id);
                    max_id++;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Integer i = rnaMapper.insertRnaFromFile(rnaArrayList);
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(new File(path)));
            String s;
            while ((s = bufferedReader.readLine()) != null) {
                String[] split = s.split("\t");
                if (split[2].equals("exon")) {
                    long START_POINT = Long.parseLong(split[3]);
                    long END_POINT = Long.parseLong(split[4]);
                    String mRNA_NAME = split[8].split("=")[1].split(";")[0];
                    int mRna_ID = saveRnaId.get(mRNA_NAME);//从缓存中获取mRnaID
                    Integer temp = saveExonNum.get(mRNA_NAME);
                    int EXON_ID = temp + 1;
                    saveExonNum.replace(mRNA_NAME, EXON_ID);
                    RNA_STRUCTURE rnaStructure = new RNA_STRUCTURE(mRna_ID, EXON_ID, START_POINT, END_POINT);
                    rnaStructures.add(rnaStructure);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        i += rnaMapper.insertRna_StructureFromFile(rnaStructures);
        return i;
    }

    @Override
    public List<RNA> findRnaByStartEND(int cs_id, String startT, String endT) {
        List<RNA> rnaList;
        if (startT != null && !startT.equals("") && endT != null && !endT.equals("")) {
            long start = Long.parseLong(startT);
            long end = Long.parseLong(endT);
            rnaList = rnaMapper.selectRnaByCS_ID_START_END(cs_id, start, end);
        } else rnaList = rnaMapper.selectRnaByCS_ID(cs_id);
        return rnaList;
    }

}