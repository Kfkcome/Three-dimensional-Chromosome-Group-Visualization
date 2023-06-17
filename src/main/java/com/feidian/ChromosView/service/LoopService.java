package com.feidian.ChromosView.service;

import com.feidian.ChromosView.domain.LoopPoint;
import com.feidian.ChromosView.domain.LoopPointMB;

import java.util.List;

public interface LoopService {
    void insertDataFromFile(String path);

    List<LoopPoint> findAllPoint(int cs_id);

    List<LoopPointMB> findPointByStart_End(int cs_id, String start, String end);
}
