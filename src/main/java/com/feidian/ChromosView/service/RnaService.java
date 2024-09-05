package com.feidian.ChromosView.service;

import com.feidian.ChromosView.exception.HicFileNotFoundException;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author kfk
 */
public interface RnaService {
    Boolean generateRnaStruct(String species, String cultivar, String tissue, String chromosome, Integer clarity, HttpServletResponse response) throws IOException, HicFileNotFoundException;

    String getPointData(String species, String cultivar, String tissue, String chromosome, int x, int y);

}
