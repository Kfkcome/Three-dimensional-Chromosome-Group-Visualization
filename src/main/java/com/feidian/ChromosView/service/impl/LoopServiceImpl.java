package com.feidian.ChromosView.service.impl;

import com.feidian.ChromosView.domain.ChromosomeT;
import com.feidian.ChromosView.domain.LoopPoint;
import com.feidian.ChromosView.domain.LoopPointMB;
import com.feidian.ChromosView.mapper.ChromosomeMapper;
import com.feidian.ChromosView.mapper.LoopMapper;
import com.feidian.ChromosView.service.LoopService;
import com.feidian.ChromosView.utils.UnitConversion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class LoopServiceImpl implements LoopService {
    private final LoopMapper loopMapper;
    private final ChromosomeMapper chromosomeMapper;

    @Autowired
    public LoopServiceImpl(LoopMapper loopMapper, ChromosomeMapper chromosomeMapper) {
        this.loopMapper = loopMapper;
        this.chromosomeMapper = chromosomeMapper;
    }

    @Override
    public void insertDataFromFile(String path) {
        File file = new File(path);
        ArrayList<LoopPoint> arrayList = new ArrayList<>();
        int i = 0;
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            String s;
            while ((s = bufferedReader.readLine()) != null) {
                String[] split = s.split("\t");
                ChromosomeT byCultivarIDCsName = chromosomeMapper.findByCultivarID_CSName(67, split[0].trim());
                LoopPoint loopPoint = new LoopPoint(0, byCultivarIDCsName.getCS_ID(), Long.parseLong(split[1]),
                        Long.parseLong(split[2]), Long.parseLong(split[4]), Long.parseLong(split[5]), Integer.parseInt(split[6])
                        , Double.parseDouble(split[7]));
                arrayList.add(loopPoint);
                i++;
            }
            loopMapper.insertFromFile(arrayList);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<LoopPoint> findAllPoint(int cs_id) {
        return loopMapper.findAllPoint(cs_id);
    }

    @Override
    public List<LoopPointMB> findPointByStart_End(int cs_id, String startT, String endT,int tissue_id,int software_id) {
        List<LoopPoint> loopPoints;
        if (startT != null && endT != null && startT != "" && endT != "") {
            Long start = Long.parseLong(startT);
            Long end = Long.parseLong(endT);
            loopPoints = loopMapper.findPointBySTART_EDN(cs_id, start, end,tissue_id,software_id);
        } else {
            loopPoints = loopMapper.findAllPoint(cs_id);
        }

        return UnitConversion.convertLoop(loopPoints);
    }

    @Override
    public List<LoopPointMB> findPointByDoublePoint(int cs_id, String start1T, String end1T, String start2T, String end2T,int tissue_id,int software_id) {
        List<LoopPoint> loopPoints;
        if (start1T != null && end1T != null && start1T != "" && end1T != "" && start2T != null && end2T != null && start2T != "" && end2T != "") {
            Long start1 = Long.parseLong(start1T);
            Long end1 = Long.parseLong(end1T);
            Long start2 = Long.parseLong(start2T);
            Long end2 = Long.parseLong(end2T);
            loopPoints = loopMapper.findPointByDoublePoint(cs_id, start1, end1, start2, end2,tissue_id,software_id);
        } else {
            loopPoints = loopMapper.findAllPoint(cs_id);
        }
        return UnitConversion.convertLoop(loopPoints);
    }

}
