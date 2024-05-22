package com.feidian.ChromosView.service;

import com.feidian.ChromosView.exception.HicFileNotFoundException;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

public interface HicService {
    Boolean generateMap(String species, String cultivar, String tissue, String chromosome, String displayOption, String normalizationType, Double maxColor, Double minColor, Integer resolution, HttpServletResponse response) throws HicFileNotFoundException;

    Boolean generateAnnotation2DMap(String species, String cultivar, String tissue, String chromosome, String displayOption, String normalizationType, Double maxColor, Double minColor, Integer clarity, Boolean loop, Boolean tad, String tadSoftware, String loopSoftware, HttpServletResponse response);

    String getPoint(String species, String cultivar, String tissue, String chromosome, int x, int y) throws HicFileNotFoundException;

    ArrayList<String> getAnnotation2DPoint(String species, String cultivar, String tissue, String chromosome, Boolean tad, Boolean loop, String tadSoftware, String loopSoftware, int x, int y) throws IOException;

    ArrayList<String> getNormalizationType(String species, String cultivar, String tissue, String chromosome) throws HicFileNotFoundException;

    ArrayList<String> getDisplayOption(String species, String cultivar, String tissue, String chromosome) throws HicFileNotFoundException;
}
