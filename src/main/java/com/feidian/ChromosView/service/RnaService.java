package com.feidian.ChromosView.service;

import com.feidian.ChromosView.domain.RNA;

import java.util.List;

/**
 * @author kfk
 */
public interface RnaService {
    Integer insertRnaFromFile(String path);

    List<RNA> findRnaByStartEND(int csId, String start, String end);

}
