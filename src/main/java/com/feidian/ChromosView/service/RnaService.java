package com.feidian.ChromosView.service;

import com.feidian.ChromosView.domain.RNA;
import com.feidian.ChromosView.domain.RNA_STRUCTURE_T;
import com.feidian.ChromosView.exception.HicFileNotFoundException;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @author kfk
 */
public interface RnaService {
    Integer insertRnaFromFile(String path);

    List<RNA> findRnaByStartEND(int csId, String start, String end, String singleLine);

    List<RNA_STRUCTURE_T> findRnaByStartEND(List<RNA> rnaList);

    Boolean generateRnaStruct(String species, String cultivar, String tissue, String chromosome, HttpServletResponse response) throws IOException, HicFileNotFoundException;

    String getPointData(String species, String cultivar, String tissue, String chromosome, int x, int y);

}
