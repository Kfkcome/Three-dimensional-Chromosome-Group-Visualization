package com.feidian.ChromosView.service.impl;

import GenerateMyHeatMap.GenerateHeatmap;
import com.feidian.ChromosView.domain.*;
import com.feidian.ChromosView.exception.HicFileNotFoundException;
import com.feidian.ChromosView.exception.QueryException;
import com.feidian.ChromosView.mapper.ChromosomeMapper;
import com.feidian.ChromosView.mapper.CultivarMapper;
import com.feidian.ChromosView.mapper.SpeciesMapper;
import com.feidian.ChromosView.service.HicService;
import com.feidian.ChromosView.utils.ReadFile;
import com.feidian.ChromosView.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class HicServiceImpl implements HicService {
    private final ChromosomeMapper chromosomeMapper;
    private final CultivarMapper cultivarMapper;
    private final RedisUtil redisUtil;
    private final SpeciesMapper speciesMapper;
    private final GenerateHeatmap generateHeatmap;

    private String uniteFileName(String species, String cultivar, String tissue) {
        return species + "_" + cultivar + "_" + tissue;
    }


    @Autowired
    public HicServiceImpl(ChromosomeMapper chromosomeMapper, CultivarMapper cultivarMapper, RedisUtil redisUtil, SpeciesMapper speciesMapper) {
        this.chromosomeMapper = chromosomeMapper;
        this.cultivarMapper = cultivarMapper;
        this.redisUtil = redisUtil;
        this.speciesMapper = speciesMapper;
        this.generateHeatmap = GenerateHeatmap.getInstance();
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
        removeFromCache(uuid);
        redisUtil.setCacheObject(uuid + "last:", nowQuery, 10L, TimeUnit.MINUTES);

        int num = 500000;
        if (matrixPoints.size() <= num) {
            redisUtil.setCacheList(uuid + "cache:", matrixPoints, 10L, TimeUnit.MINUTES);
        } else {
            int start = 0;
            int end = num;
            for (int i = 0; i < matrixPoints.size() / num + 1; i++) {
                if (end >= matrixPoints.size()) {
                    end = matrixPoints.size() - 1;
                }
                List<MatrixPoint> list = matrixPoints.subList(start, end);
                redisUtil.setCacheList(uuid + "cache:", list, 10L, TimeUnit.MINUTES);
                start = end;
                end += num;
            }
        }
        redisUtil.setCacheObject(uuid + "index:", 1, 10L, TimeUnit.MINUTES);
    }

    void removeFromCache(String uuid) {
        redisUtil.deleteObject(uuid + "cache");
        redisUtil.deleteObject(uuid + "cache");
        redisUtil.deleteObject(uuid + "last");
    }

    @Override
    public UUID_matrixPoints findByCS_ID(String uuid, int cs_id1, int cs_id2, String norms, String binXStart, String binYStart, String binXEnd, String binYEnd, String resolution, Integer tissue_id) throws QueryException, FileNotFoundException {
        LastQuery nowQuery = new LastQuery(cs_id1, cs_id2, norms, binXStart, binYStart, binXEnd, binYEnd, resolution);
        LastQuery lastQuery = redisUtil.getCacheObject(uuid + "last:");
        List<MatrixPoint> object = redisUtil.getCacheList(uuid + "cache:");
        Integer index = redisUtil.getCacheObject(uuid + "index:");
        //从缓存中读取数据
        if (object != null && nowQuery.equals(lastQuery)) {
            System.out.println("分页查询 当前页数：" + index + "总共页数： " + object.size() / 5000);
            int last = 5000 * (index + 1) - 1;
            int front = 5000 * index;
            if (front >= object.size()) {
                removeFromCache(uuid);
                throw new QueryException("已经到达最大索引");
            } else if (last >= object.size()) {
                last = object.size() - 1;
            }
            redisUtil.setCacheObject(uuid + "index:", index + 1);
            return new UUID_matrixPoints(object.subList(front, last), index, object.size() / 5000, uuid);
        }
        if (uuid == null || uuid.isEmpty()) {
            uuid = String.valueOf(UUID.randomUUID());
        } else {
            throw new QueryException("uuid错误，无法查询到数据");
        }
        //直接从文件中解析
        System.out.println("直接查询");
        ChromosomeT byCSId = chromosomeMapper.findByCS_ID(cs_id1);
        ChromosomeT byCSId2 = chromosomeMapper.findByCS_ID(cs_id2);
        String CSName1 = byCSId.getCS_NAME();
        byCSId.getCULTIVAR_ID();
        byCSId2.getCULTIVAR_ID();
        String CSName2 = byCSId2.getCS_NAME();
        Cultivar cultivar = cultivarMapper.findOneByTissueId(byCSId.getCULTIVAR_ID());
        Species species = speciesMapper.findById(cultivar.getSPECIES_ID());
        Tissue tissue = cultivarMapper.findTissueByID(tissue_id);
        System.out.println("要查找的染色体的名字" + CSName1 + " " + CSName2);

        Long binXS, binXE, binYS, binYE;
        String file_name = species.getSPECIES_NAME() + "_" + cultivar.getCULTIVAR_NAME() + "_" + tissue.getTISSUE_NAME();
        File file = new File("./" + file_name + "/" + file_name + ".hic");
        if (!file.exists()) {
            throw new FileNotFoundException(file.getPath());
        }
        List<MatrixPoint> matrixPoints = new ArrayList<>();
        if (norms == null || norms.equals("NONE"))
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
        //TODO:不够5000条的判断
        if (matrixPoints.size() >= 5000) {
            addToCache(uuid, nowQuery, matrixPoints);
            return new UUID_matrixPoints(matrixPoints.subList(0, 4999), 0, matrixPoints.size() / 5000, uuid);
        } else return new UUID_matrixPoints(matrixPoints, 0, 0, "none");
    }

    @Override
    public String getPoint(String species, String cultivar, String tissue, String chromosome, int x, int y) throws HicFileNotFoundException {
        String fileName = uniteFileName(species, cultivar, tissue);
        String path = fileName + "/" + fileName + ".hic";
        String s;
        try {
            s = generateHeatmap.getPointData(path, chromosome, x, y);
            System.out.println(s);
        } catch (Exception e) {
            throw new HicFileNotFoundException(e.getMessage());
        }
        return s;
    }

    @Override
    public ArrayList<String> getAnnotation2DPoint(String species, String cultivar, String tissue, String chromosome, Boolean loop, Boolean tad, int x, int y) throws IOException {
        ArrayList<String> annotation = new ArrayList<>();
        if (loop)
            annotation.add("./loop.bedpe");
        if (tad)
            annotation.add("./TAD.bedpe");
        ArrayList<String> annotationPoint = generateHeatmap.getAnnotationPoint("./Gossypium-hirsutum_TM-1_Leaf.hic", annotation, chromosome, x, y);
        for (String s : annotationPoint) {
            System.out.println(s);
        }
        return annotationPoint;
    }

    @Override
    public ArrayList<String> getNormalizationType(String species, String cultivar, String tissue, String chromosome) throws HicFileNotFoundException {
        String fileName = uniteFileName(species, cultivar, tissue);
        String path = fileName + "/" + fileName + ".hic";
        try {
            return generateHeatmap.getNormalizationType(path, chromosome);
        } catch (Exception e) {
            throw new HicFileNotFoundException(e.getMessage());
        }
    }

    @Override
    public ArrayList<String> getDisplayOption(String species, String cultivar, String tissue, String chromosome) throws HicFileNotFoundException {
        String fileName = uniteFileName(species, cultivar, tissue);
        String path = fileName + "/" + fileName + ".hic";
        try {
            return generateHeatmap.getDisplayOption(path, chromosome);
        } catch (Exception e) {
            throw new HicFileNotFoundException(e.getMessage());
        }
    }


    @Override
    public Boolean generateMap(String species, String cultivar, String tissue, String chromosome, String displayOption, String normalizationType, HttpServletResponse response) throws HicFileNotFoundException {
        //todo:实现对物种的搜索
//        String csName = chromosomeMapper.findByCS_ID(cs_id).getCS_NAME();
//        String csName = "SoyC02.Chr02";
        String fileName = uniteFileName(species, cultivar, tissue);
        String path = fileName + "/" + fileName + ".hic";
        BufferedImage image;
        try {
            image = generateHeatmap.generateFullHeatMap(path, chromosome, displayOption, normalizationType);
        } catch (Exception e) {
            throw new HicFileNotFoundException(e.getMessage());
        }
        try {
            OutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
            ImageIO.write(image, "png", outputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
            //todo:实现生成图片的异常处理
        }
        return true;
    }

    @Override
    public Boolean generateAnnotation2DMap(String species, String cultivar, String tissue, String chromosome, String displayOption, String normalizationType, Boolean tad, Boolean loop, HttpServletResponse response) {
        BufferedImage image;
        ArrayList<String> annotation = new ArrayList<>();
        if (loop)
            annotation.add("./loop.bedpe");
        if (tad)
            annotation.add("./TAD.bedpe");
        try {
            image = generateHeatmap.generateAnnotation2D("./Gossypium-hirsutum_TM-1_Leaf.hic", annotation, chromosome, displayOption, normalizationType);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            OutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
            ImageIO.write(image, "png", outputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
            //todo:实现生成图片的异常处理
        }
        return true;
    }


    public void writeTOFile(BufferedImage bufferedImage) {
        File outputFile = new File("/home/new/test_new" + ".png");
        try {
            ImageIO.write(bufferedImage, "png", outputFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
