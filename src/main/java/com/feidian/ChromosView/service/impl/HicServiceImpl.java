package com.feidian.ChromosView.service.impl;

import com.feidian.ChromosView.domain.CacheMapper;
import com.feidian.ChromosView.domain.ChromosomeT;
import com.feidian.ChromosView.domain.LastQuery;
import com.feidian.ChromosView.domain.MatrixPoint;
import com.feidian.ChromosView.exception.QueryException;
import com.feidian.ChromosView.mapper.ChromosomeMapper;
import com.feidian.ChromosView.service.HicService;
import com.feidian.ChromosView.utils.ReadFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Service
public class HicServiceImpl implements HicService {
    private final ChromosomeMapper chromosomeMapper;
    private final CacheMapper cacheMapper;

    @Autowired
    public HicServiceImpl(ChromosomeMapper chromosomeMapper, CacheMapper cacheMapper) {
        this.chromosomeMapper = chromosomeMapper;
        this.cacheMapper = cacheMapper;
    }


    public boolean checkNorms(String norms) {
        String[] strings = {"KR", "SCALE", "VC", "VC_SQRT", "NONE"};
        for (String string : strings) {
            if (norms.equals(string))
                return true;
        }
        return false;
    }

    @Override
    public List<MatrixPoint> findByCS_ID(int cs_id, String norms, String binXStart, String binYStart, String binXEnd, String binYEnd) throws QueryException {
        LastQuery nowQuery = new LastQuery(cs_id, norms, binXStart, binYStart, binXEnd, binYEnd);
        LastQuery lastQuery = (LastQuery) cacheMapper.getObject("last:");
        List<MatrixPoint> object = (List<MatrixPoint>) cacheMapper.getObject("cache:");
        if (object != null && lastQuery.equals(nowQuery)) {
            Integer index = (Integer) cacheMapper.getObject("index:");
            int last = 500 * (index + 1) - 1;
            int front = 500 * index;
            if (front >= object.size()) {
                //TODO: index越界异常
                cacheMapper.deleteObject("cache:");
                cacheMapper.deleteObject("index:");
                cacheMapper.deleteObject("last:");
                throw new QueryException("已经到达最大索引");
            } else if (last >= object.size()) {
                last = object.size() - 1;
            }
            cacheMapper.updateObject("index:", index + 1);
            return object.subList(front, last);

        }

        ChromosomeT byCSId = chromosomeMapper.findByCS_ID(cs_id);
        String CSName = byCSId.getCS_NAME().split("A2.")[1];
        System.out.println("要查找的染色体的名字" + CSName);
        Long binXS, binXE, binYS, binYE;
        File file = new File("D:/DNA/A2_matrix.hic");
        List<MatrixPoint> matrixPoints = new ArrayList<>();
        if (norms == null || norms.equals("NONE"))
            norms = new String("SCALE");
        if (!checkNorms(norms))
            return matrixPoints;
        if (binXStart != null && binYStart != null && binXEnd != null && binYEnd != null) {
            try {
                binXS = Long.parseLong(binXStart);
                binXE = Long.parseLong(binXEnd);
                binYS = Long.parseLong(binYStart);
                binYE = Long.parseLong(binYEnd);
            } catch (NumberFormatException numberFormatException) {
                return ReadFile.readHICALL(file, CSName, norms);
            }
            matrixPoints = ReadFile.readHICByStart_End(file, CSName, norms, binXS, binYS, binXE, binYE);
        } else {
            matrixPoints = ReadFile.readHICALL(file, CSName, norms);
        }
        cacheMapper.deleteObject("cache:");
        cacheMapper.deleteObject("index:");
        cacheMapper.deleteObject("last:");
        cacheMapper.addObject("last:", nowQuery);
        cacheMapper.addObject("cache:", matrixPoints);
        cacheMapper.addObject("index:", 1);
        return matrixPoints.subList(0, 499);
    }
}
