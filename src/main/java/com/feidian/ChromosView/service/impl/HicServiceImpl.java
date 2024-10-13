package com.feidian.ChromosView.service.impl;

import GenerateMyHeatMap.GenerateHeatmap;
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
    public Boolean generateMap(String species, String cultivar, String tissue, String chromosome, String displayOption, String normalizationType, Double minColor, Double maxColor, Integer clarity, Integer resolution, HttpServletResponse response) throws HicFileNotFoundException {
        //todo:实现对物种的搜索
//        String csName = chromosomeMapper.findByCS_ID(cs_id).getCS_NAME();
//        String csName = "SoyC02.Chr02";
        if (clarity == null) {
            clarity = 1;//默认分辨率为1
        }
        if (resolution == null) {
            resolution = 10000;//默认分辨率为10000
        }
        maxColor = maxColor == null ? 100 : maxColor;
        minColor = minColor == null ? 1 : minColor;

        String fileName = uniteFileName(species, cultivar, tissue);
        String path = "hic/" + fileName + ".hic";
        BufferedImage image;
        try {
            image = generateHeatmap.generateFullHeatMap(1, path, chromosome, displayOption, normalizationType, minColor, maxColor, clarity, resolution);
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
    public Long getChromosomeLength(String species, String cultivar, String tissue, String chromosome) throws HicFileNotFoundException {
        String fileName = uniteFileName(species, cultivar, tissue);
        String path = "hic/" + fileName + ".hic";
        try {
            return generateHeatmap.getChromosomeLength(path, chromosome);
        } catch (Exception e) {
            throw new HicFileNotFoundException(e.getMessage());
        }
    }

    @Override
    public Boolean generateAnnotation2DMap(String species, String cultivar, String tissue, String chromosome, String displayOption, String normalizationType, Double minColor, Double maxColor, Integer clarity, Integer resolution, Boolean tad, Boolean loop, String tadSoftware, String loopSoftware, HttpServletResponse response) {
        BufferedImage image;
        String fileName = uniteFileName(species, cultivar, tissue);
        String path = "hic/" + fileName + ".hic";
        ArrayList<String> annotation = new ArrayList<>();
        if (clarity == null) {
            clarity = 1;//默认分辨率为1
        }
        maxColor = maxColor == null ? 100 : maxColor;
        minColor = minColor == null ? 1 : minColor;
        if (loop) {
            annotation.add("Loop/" + loopSoftware + "/" + fileName + ".bedpe");
        }
        if (tad) {
            annotation.add("TAD/" + tadSoftware + "/" + fileName + ".bedpe");
        }
        try {
            image = generateHeatmap.generateAnnotation2D(path, annotation, chromosome, displayOption, normalizationType, minColor, maxColor, clarity, resolution);
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
