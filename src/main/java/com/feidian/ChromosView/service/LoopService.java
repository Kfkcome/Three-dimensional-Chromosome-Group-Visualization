package com.feidian.ChromosView.service;

import com.feidian.ChromosView.domain.LoopPoint;
import com.feidian.ChromosView.domain.LoopPointMB;

import java.util.List;

public interface LoopService {
    void insertDataFromFile(String path);

    List<LoopPoint> findAllPoint(int cs_id);

    List<LoopPointMB> findPointByStart_End(int cs_id, String start, String end,int tissue_id,int software_id);

    List<LoopPointMB> findPointByDoublePoint(int cs_id, String start1, String end1, String start2, String end2,int tissue_id,int software_id);
}
