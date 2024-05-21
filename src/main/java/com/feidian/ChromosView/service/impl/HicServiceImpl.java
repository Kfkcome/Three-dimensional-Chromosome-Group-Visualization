package com.feidian.ChromosView.service.impl;

import GenerateMyHeatMap.GenerateHeatmap;
import com.feidian.ChromosView.domain.LastQuery;
import com.feidian.ChromosView.domain.MatrixPoint;
import com.feidian.ChromosView.exception.HicFileNotFoundException;
import com.feidian.ChromosView.mapper.ChromosomeMapper;
import com.feidian.ChromosView.mapper.CultivarMapper;
import com.feidian.ChromosView.mapper.SpeciesMapper;
import com.feidian.ChromosView.service.HicService;
import com.feidian.ChromosView.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class HicServiceImpl implements HicService {
    private final ChromosomeMapper chromosomeMapper;
    private final CultivarMapper cultivarMapper;
    private final RedisUtil redisUtil;
    private final SpeciesMapper speciesMapper;
    private final GenerateHeatmap generateHeatmap;

    @Autowired
    public HicServiceImpl(ChromosomeMapper chromosomeMapper, CultivarMapper cultivarMapper, RedisUtil redisUtil, SpeciesMapper speciesMapper) {
        this.chromosomeMapper = chromosomeMapper;
        this.cultivarMapper = cultivarMapper;
        this.redisUtil = redisUtil;
        this.speciesMapper = speciesMapper;
        this.generateHeatmap = GenerateHeatmap.getInstance();
    }

    private String uniteFileName(String species, String cultivar, String tissue) {
        return species + "_" + cultivar + "_" + tissue;
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
    public String getPoint(String species, String cultivar, String tissue, String chromosome, int x, int y) throws HicFileNotFoundException {
        String fileName = uniteFileName(species, cultivar, tissue);
        String path = "hic/" + fileName + ".hic";
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
    public ArrayList<String> getAnnotation2DPoint(String species, String cultivar, String tissue, String chromosome, Boolean tad, Boolean loop, String tadSoftware, String loopSoftware, int x, int y) throws IOException {
        String fileName = uniteFileName(species, cultivar, tissue);
        String path = "hic/" + fileName + ".hic";
        ArrayList<String> annotation = new ArrayList<>();
        if (loop) {
            annotation.add("Loop/" + loopSoftware + "/" + fileName + ".bedpe");
        }
        if (tad) {
            annotation.add("TAD/" + tadSoftware + "/" + fileName + ".bedpe");
        }
        ArrayList<String> annotationPoint = generateHeatmap.getAnnotationPoint(path, annotation, chromosome, x, y);
        for (String s : annotationPoint) {
            System.out.println(s);
        }
        return annotationPoint;
    }

    @Override
    public ArrayList<String> getNormalizationType(String species, String cultivar, String tissue, String chromosome) throws HicFileNotFoundException {
        String fileName = uniteFileName(species, cultivar, tissue);
        String path = "hic/" + fileName + ".hic";
        try {
            return generateHeatmap.getNormalizationType(path, chromosome);
        } catch (Exception e) {
            throw new HicFileNotFoundException(e.getMessage());
        }
    }

    @Override
    public ArrayList<String> getDisplayOption(String species, String cultivar, String tissue, String chromosome) throws HicFileNotFoundException {
        String fileName = uniteFileName(species, cultivar, tissue);
        String path = "hic/" + fileName + ".hic";
        try {
            return generateHeatmap.getDisplayOption(path, chromosome);
        } catch (Exception e) {
            throw new HicFileNotFoundException(e.getMessage());
        }
    }


    @Override
    public Boolean generateMap(String species, String cultivar, String tissue, String chromosome, String displayOption, String normalizationType, Integer colorValue, Integer clarity, HttpServletResponse response) throws HicFileNotFoundException {
        //todo:实现对物种的搜索
//        String csName = chromosomeMapper.findByCS_ID(cs_id).getCS_NAME();
//        String csName = "SoyC02.Chr02";
        if (clarity == null) {
            clarity = 1;//默认分辨率为1
        }
        if (colorValue == null) {
            colorValue = 1;//默认颜色值为1
        }

        String fileName = uniteFileName(species, cultivar, tissue);
        String path = "hic/" + fileName + ".hic";
        BufferedImage image;
        try {
            image = generateHeatmap.generateFullHeatMap(path, chromosome, displayOption, normalizationType, colorValue, clarity);
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
    public Boolean generateAnnotation2DMap(String species, String cultivar, String tissue, String chromosome, String displayOption, String normalizationType, Integer colorValue, Integer clarity, Boolean tad, Boolean loop, String tadSoftware, String loopSoftware, HttpServletResponse response) {
        BufferedImage image;
        String fileName = uniteFileName(species, cultivar, tissue);
        String path = "hic/" + fileName + ".hic";
        ArrayList<String> annotation = new ArrayList<>();
        if (clarity == null) {
            clarity = 1;//默认分辨率为1
        }
        if (colorValue == null) {
            colorValue = 1;//默认颜色值为1
        }
        if (loop) {
            annotation.add("Loop/" + loopSoftware + "/" + fileName + ".bedpe");
        }
        if (tad) {
            annotation.add("TAD/" + tadSoftware + "/" + fileName + ".bedpe");
        }
        try {
            image = generateHeatmap.generateAnnotation2D(path, annotation, chromosome, displayOption, normalizationType, colorValue, clarity);
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
