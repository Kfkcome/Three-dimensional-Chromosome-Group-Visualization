package com.feidian.ChromosView.service.impl;

import GenerateMyHeatMap.GenerateHeatmap;
import com.feidian.ChromosView.exception.HicFileNotFoundException;
import com.feidian.ChromosView.service.RnaService;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

@Service
public class RnaServiceImpl implements RnaService {
    private final GenerateHeatmap generateHeatmap = GenerateHeatmap.getInstance();

    private int checkDirection(String s) {
        if (s.equals("-")) {
            return 0;
        }
        return 1;
    }

    private String uniteFileNameToHICPath(String species, String cultivar, String tissue) {
        return species + "_" + cultivar + "_" + tissue;
    }

    private String uniteFileNameToAnnotationPath(String species, String cultivar) {
        return species + "_" + cultivar;
    }

    @Override
    public Boolean generateRnaStruct(String species, String cultivar, String tissue, String chromosome, Integer clarity, HttpServletResponse response) throws HicFileNotFoundException {
        Boolean generateSuccess = true;
        String fileName = uniteFileNameToHICPath(species, cultivar, tissue);
        String path = "hic/" + fileName + ".hic";
        String annotationName = uniteFileNameToAnnotationPath(species, cultivar);
        String annotationPath = "Gene/" + annotationName + ".bed.gz";
        if (clarity == null) {
            clarity = 1;//默认分辨率为1
        }
        File file = new File(annotationPath);
        if (!file.exists()) {
            generateSuccess = false;
            return generateSuccess;
        }
        BufferedImage image;
        try {
            image = generateHeatmap.generateAnnotation1D(path, annotationPath, chromosome, clarity);
        } catch (IOException e) {
            throw new HicFileNotFoundException(e.getMessage());
        }
        if (image == null) {
            return false;
        }
        try {
            OutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
            ImageIO.write(image, "png", outputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
            //todo:实现生成图片的异常处理
        }
        return generateSuccess;
    }

    @Override
    public String getPointData(String species, String cultivar, String tissue, String chromosome, int x, int y) {
        String fileName = uniteFileNameToHICPath(species, cultivar, tissue);
        String path = "hic/" + fileName + ".hic";
        String annotationName = uniteFileNameToAnnotationPath(species, cultivar);
        String annotationPath = "Gene/" + annotationName + ".bed.gz";
        String data = null;
        try {
            data = generateHeatmap.getAnnotation1DData(path, annotationPath, chromosome, x, y);
        } catch (IOException e) {
            //TODO：处理找不到点的异常
        }
        return data;
    }

    public void writeToFile(BufferedImage bufferedImage, String name) {
        File outputFile = new File("/home/new/test_new" + name + ".png");
        try {
            ImageIO.write(bufferedImage, "png", outputFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
