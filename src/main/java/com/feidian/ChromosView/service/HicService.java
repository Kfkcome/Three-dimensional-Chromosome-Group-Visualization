package com.feidian.ChromosView.service;

import com.feidian.ChromosView.domain.UUID_matrixPoints;
import com.feidian.ChromosView.exception.QueryException;

import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;

public interface HicService {
    public BufferedImage generateMap(int cs_id, Integer tissue_id);

    public UUID_matrixPoints findByCS_ID(String uuid, int cs_id1, int cs_id2, String norms, String binXStart, String binYStart, String binXEnd, String binYEnd, String resolution, Integer tissue_id) throws QueryException, FileNotFoundException;
}
