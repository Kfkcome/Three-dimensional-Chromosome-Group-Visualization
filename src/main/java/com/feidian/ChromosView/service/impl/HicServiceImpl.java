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

    void addToCache(LastQuery nowQuery, List<MatrixPoint> matrixPoints) {
        cacheMapper.deleteObject("cache:");
        cacheMapper.deleteObject("index:");
        cacheMapper.deleteObject("last:");
        cacheMapper.addObject("last:", nowQuery);
        cacheMapper.addObject("cache:", matrixPoints);
        cacheMapper.addObject("index:", 1);
    }

    void removeFromCache() {
        cacheMapper.deleteObject("cache:");
        cacheMapper.deleteObject("index:");
        cacheMapper.deleteObject("last:");
    }

    @Override
    public List<MatrixPoint> findByCS_ID(int cs_id1, int cs_id2, String norms, String binXStart, String binYStart, String binXEnd, String binYEnd, String resolution) throws QueryException {
        LastQuery nowQuery = new LastQuery(cs_id1, cs_id2, norms, binXStart, binYStart, binXEnd, binYEnd, resolution);
        LastQuery lastQuery = (LastQuery) cacheMapper.getObject("last:");
        List<MatrixPoint> object = (List<MatrixPoint>) cacheMapper.getObject("cache:");
        //从缓存中读取数据
        if (object != null && lastQuery.equals(nowQuery)) {

            Integer index = (Integer) cacheMapper.getObject("index:");
            System.out.println("分页查询 当前页数：" + index + "总共页数： " + object.size() / 5000);
            int last = 5000 * (index + 1) - 1;
            int front = 5000 * index;
            if (front >= object.size()) {
                removeFromCache();
                throw new QueryException("已经到达最大索引");
            } else if (last >= object.size()) {
                last = object.size() - 1;
            }
            cacheMapper.updateObject("index:", index + 1);
            return object.subList(front, last);
        }

        //直接从文件中解析
        System.out.println("直接查询");
        ChromosomeT byCSId = chromosomeMapper.findByCS_ID(cs_id1);
        ChromosomeT byCSId2 = chromosomeMapper.findByCS_ID(cs_id2);
        String CSName1 = byCSId.getCS_NAME().split("A2.")[1];
        String CSName2 = byCSId2.getCS_NAME().split("A2.")[1];
        System.out.println("要查找的染色体的名字" + CSName1 + " " + CSName2);
        Long binXS, binXE, binYS, binYE;
        File file = new File("D:/DNA/A2_matrix.hic");
        List<MatrixPoint> matrixPoints = new ArrayList<>();
        if (norms == null || norms.equals("NONE"))
            norms = "SCALE";
        if (!checkNorms(norms))
            return matrixPoints;
        if (binXStart != null && binYStart != null && binXEnd != null && binYEnd != null) {
            try {
                binXS = Long.parseLong(binXStart);
                binXE = Long.parseLong(binXEnd);
                binYS = Long.parseLong(binYStart);
                binYE = Long.parseLong(binYEnd);
            } catch (NumberFormatException numberFormatException) {
                matrixPoints = ReadFile.readHICALL(file, CSName1, CSName2, norms, resolution);
                //TODO:不够5000条的判断
                if (matrixPoints.size() >= 5000) {
                    addToCache(nowQuery, matrixPoints);
                    return matrixPoints.subList(0, 4999);
                } else return matrixPoints;
            }
            matrixPoints = ReadFile.readHICByStart_End(file, CSName1, CSName2, norms, binXS, binYS, binXE, binYE, resolution);
        } else {
            matrixPoints = ReadFile.readHICALL(file, CSName1, CSName2, norms, resolution);
        }
        addToCache(nowQuery, matrixPoints);
        //System.out.println(matrixPoints.size());
        //TODO:不够5000条的判断
        if (matrixPoints.size() >= 5000) {
            addToCache(nowQuery, matrixPoints);
            return matrixPoints.subList(0, 4999);
        } else return matrixPoints;
    }

}
