package com.feidian.ChromosView.service;

import com.feidian.ChromosView.domain.UUID_matrixPoints;
import com.feidian.ChromosView.exception.HicFileNotFoundException;
import com.feidian.ChromosView.exception.QueryException;

import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public interface HicService {
    Boolean generateMap(String species, String cultivar, String tissue, String chromosome, String displayOption, String normalizationType, HttpServletResponse response) throws HicFileNotFoundException;

    Boolean generateAnnotation2DMap(String species, String cultivar, String tissue, String chromosome, String displayOption, String normalizationType, Boolean loop, Boolean tad, String tadSoftware, String loopSoftware, HttpServletResponse response);

    UUID_matrixPoints findByCS_ID(String uuid, int cs_id1, int cs_id2, String norms, String binXStart, String binYStart, String binXEnd, String binYEnd, String resolution, Integer tissue_id) throws QueryException, FileNotFoundException;

    String getPoint(String species, String cultivar, String tissue, String chromosome, int x, int y) throws HicFileNotFoundException;

    ArrayList<String> getAnnotation2DPoint(String species, String cultivar, String tissue, String chromosome, Boolean tad, Boolean loop, String tadSoftware, String loopSoftware, int x, int y) throws IOException;

    ArrayList<String> getNormalizationType(String species, String cultivar, String tissue, String chromosome) throws HicFileNotFoundException;

    ArrayList<String> getDisplayOption(String species, String cultivar, String tissue, String chromosome) throws HicFileNotFoundException;
}
