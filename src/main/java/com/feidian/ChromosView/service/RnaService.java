package com.feidian.ChromosView.service;

import com.feidian.ChromosView.domain.RNA;
import com.feidian.ChromosView.domain.RNA_STRUCTURE_T;

import java.util.List;

/**
 * @author kfk
 */
public interface RnaService {
    Integer insertRnaFromFile(String path);

    List<RNA> findRnaByStartEND(int csId, String start, String end, String singleLine);

    List<RNA_STRUCTURE_T> findRnaByStartEND(List<RNA> rnaList);

}
