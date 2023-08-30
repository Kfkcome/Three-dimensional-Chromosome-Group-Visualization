package com.feidian.ChromosView.service.impl;

import com.feidian.ChromosView.domain.*;
import com.feidian.ChromosView.domain.redis.RedisCache;
import com.feidian.ChromosView.exception.QueryException;
import com.feidian.ChromosView.mapper.ChromosomeMapper;
import com.feidian.ChromosView.service.HicService;
import com.feidian.ChromosView.utils.ReadFile;
import com.feidian.ChromosView.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class HicServiceImpl implements HicService {
    private final ChromosomeMapper chromosomeMapper;
    private final CacheMapper cacheMapper;
    private final RedisUtil redisUtil;


    @Autowired
    public HicServiceImpl(ChromosomeMapper chromosomeMapper, CacheMapper cacheMapper, RedisUtil redisUtil) {
        this.chromosomeMapper = chromosomeMapper;
        this.cacheMapper = cacheMapper;
        this.redisUtil = redisUtil;
    }


    public boolean checkNorms(String norms) {
        String[] strings = {"KR", "SCALE", "VC", "VC_SQRT", "NONE"};
        for (String string : strings) {
            if (norms.equals(string))
                return true;
        }
        return false;
    }

    void addToCache(String uuid, LastQuery nowQuery, List<MatrixPoint> matrixPoints) {
        redisUtil.deleteObject(uuid+"cache");
        redisUtil.deleteObject(uuid+"cache");
        cacheMapper.deleteObject(uuid + "cache:");
        cacheMapper.deleteObject(uuid + "index:");
        cacheMapper.deleteObject(uuid + "last:");
        cacheMapper.addObject(uuid + "last:", nowQuery);
        cacheMapper.addObject(uuid + "cache:", matrixPoints);
        cacheMapper.addObject(uuid + "index:", 1);
    }

    void removeFromCache(String uuid) {
        cacheMapper.deleteObject(uuid + "cache:");
        cacheMapper.deleteObject(uuid + "index:");
        cacheMapper.deleteObject(uuid + "last:");
    }

    @Override
    public UUID_matrixPoints findByCS_ID(String uuid, int cs_id1, int cs_id2, String norms, String binXStart, String binYStart, String binXEnd, String binYEnd, String resolution) throws QueryException {
        LastQuery nowQuery = new LastQuery(cs_id1, cs_id2, norms, binXStart, binYStart, binXEnd, binYEnd, resolution);
        LastQuery lastQuery = (LastQuery) cacheMapper.getObject(uuid + "last:");
        List<MatrixPoint> object = (List<MatrixPoint>) cacheMapper.getObject(uuid + "cache:");
        Integer index = (Integer) cacheMapper.getObject(uuid + "index:");
        //从缓存中读取数据
        if (object != null && lastQuery.equals(nowQuery)) {
            System.out.println("分页查询 当前页数：" + index + "总共页数： " + object.size() / 5000);
            int last = 5000 * (index + 1) - 1;
            int front = 5000 * index;
            if (front >= object.size()) {
                removeFromCache(uuid);
                throw new QueryException("已经到达最大索引");
            } else if (last >= object.size()) {
                last = object.size() - 1;
            }
            cacheMapper.updateObject(uuid + "index:", index + 1);
            return new UUID_matrixPoints(object.subList(front, last), index, object.size() / 5000, uuid);
        }
        if (uuid == null || (uuid != null && uuid.equals(""))) {
            uuid = String.valueOf(UUID.randomUUID());
        } else {
            throw new QueryException("uuid错误，无法查询到数据");
        }
        //直接从文件中解析
        System.out.println("直接查询");
        ChromosomeT byCSId = chromosomeMapper.findByCS_ID(cs_id1);
        ChromosomeT byCSId2 = chromosomeMapper.findByCS_ID(cs_id2);
        String CSName1 = byCSId.getCS_NAME();
        String CSName2 = byCSId2.getCS_NAME();
        System.out.println("要查找的染色体的名字" + CSName1 + " " + CSName2);
        Long binXS, binXE, binYS, binYE;
        File file = new File("/home/new/fsdownload/Arabidopsis-thaliana_Col-0_Root.hic");
        List<MatrixPoint> matrixPoints = new ArrayList<>();
        if (norms == null || norms.equals("NONE"))
            norms = "SCALE";
        if (!checkNorms(norms))
            return new UUID_matrixPoints(matrixPoints, index, 0, uuid);
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
                    addToCache(uuid, nowQuery, matrixPoints);
                    return new UUID_matrixPoints(matrixPoints.subList(0, 4999), 0, matrixPoints.size() / 5000, uuid);
                } else return new UUID_matrixPoints(matrixPoints, 0, 0, "none");
            }
            matrixPoints = ReadFile.readHICByStart_End(file, CSName1, CSName2, norms, binXS, binYS, binXE, binYE, resolution);
        } else {
            matrixPoints = ReadFile.readHICALL(file, CSName1, CSName2, norms, resolution);
        }
        addToCache(uuid, nowQuery, matrixPoints);
        //System.out.println(matrixPoints.size());
        //TODO:不够5000条的判断
        if (matrixPoints.size() >= 5000) {
            addToCache(uuid, nowQuery, matrixPoints);
            return new UUID_matrixPoints(matrixPoints.subList(0, 4999), 0, matrixPoints.size() / 5000, uuid);
        } else return new UUID_matrixPoints(matrixPoints, 0, 0, "none");
    }
}
