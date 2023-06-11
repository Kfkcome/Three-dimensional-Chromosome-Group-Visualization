package com.feidian.ChromosView.service;

import com.feidian.ChromosView.domain.LoopPoint;

import java.util.List;

public interface LoopService {
    void  insertDataFromFile(String path);
    List<LoopPoint> findAllPoint(int cs_id);
}
