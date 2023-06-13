package com.feidian.ChromosView.service.impl;

import com.feidian.ChromosView.domain.Chromosome;
import com.feidian.ChromosView.domain.CompartmentPoint;
import com.feidian.ChromosView.domain.LoopPoint;
import com.feidian.ChromosView.mapper.ChromosomeMapper;
import com.feidian.ChromosView.mapper.LoopMapper;
import com.feidian.ChromosView.service.LoopService;
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
        File file=new File(path);
        ArrayList<LoopPoint> arrayList=new ArrayList<>();
        int i=0;
        try {
            BufferedReader bufferedReader=new BufferedReader(new FileReader(file));
            String s;
            while((s = bufferedReader.readLine())!=null){
                String[] split = s.split("\t");
                Chromosome byCultivarIDCsName = chromosomeMapper.findByCultivarID_CSName(67, split[0].trim());
                LoopPoint loopPoint=new LoopPoint(0,byCultivarIDCsName.getCS_ID(),Long.parseLong(split[1]),
                        Long.parseLong(split[2]),Long.parseLong(split[4]),Long.parseLong(split[5]),Integer.parseInt(split[6])
                        ,Double.parseDouble(split[7]));
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
    public List<LoopPoint> findPointByStart_End(int cs_id, long start, long end) {
        return loopMapper.findPointBySTART_EDN(cs_id,start,end);
    }

}
