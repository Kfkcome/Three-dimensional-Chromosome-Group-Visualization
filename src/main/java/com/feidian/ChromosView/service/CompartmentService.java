package com.feidian.ChromosView.service;

import com.feidian.ChromosView.domain.CompartmentPoint;
import com.feidian.ChromosView.domain.CompartmentPointMB;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CompartmentService {
    int addCS();
    int addPointFromFile(String path);
    List<CompartmentPoint> findPointByCS_ID(int id,int tissue_id,int software_id);
    List<CompartmentPoint> findPointByEND_START(int cs_id,String startT,String endT,int tissue_id,int software_id);
}
