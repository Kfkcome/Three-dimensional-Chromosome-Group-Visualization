package com.feidian.ChromosView.service.impl;

import GenerateMyHeatMap.GenerateHeatmap;
import com.feidian.ChromosView.service.CompartmentService;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;

@Service
public class CompartmentServiceImpl implements CompartmentService {

    private final GenerateHeatmap generateHeatmap = GenerateHeatmap.getInstance();


    private String uniteFileNameToHICPath(String species, String cultivar, String tissue) {
        return species + "_" + cultivar + "_" + tissue;
    }

    @Override
    public Boolean generateCompartment(String species, String cultivar, String tissue, String chromosome, Integer clarity, HttpServletResponse response) {
        BufferedImage temp;
        String fileName = uniteFileNameToHICPath(species, cultivar, tissue);
        String path = "hic/" + fileName + ".hic";
        String annotationName = uniteFileNameToHICPath(species, cultivar, tissue);
        String annotationPath = "Compartment/" + annotationName + ".bw";
        if (clarity == null) {
            clarity = 1;//默认分辨率为1
        }
        try {
            temp = generateHeatmap.generateAnnotation1D(path, annotationPath, chromosome, clarity);
        } catch (IOException e) {
            throw new RuntimeException(e);
            //TODO：处理生成Compartment图生成失败
        }
        if (temp == null) {
            return false;
        }
        try {
            OutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
            ImageIO.write(temp, "png", outputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
            //todo:实现生成图片的异常处理
        }
        return true;
    }

    @Override
    public String getPointData(String species, String cultivar, String tissue, String chromosome, int x, int y) {
        String data = null;
        try {
            data = generateHeatmap.getAnnotation1DData("./Gossypium-hirsutum_TM-1_Leaf.hic", "./Compartment.bw", chromosome, x, y);
        } catch (IOException e) {
            //TODO：处理找不到点的异常
        }
        return data;
    }

}
