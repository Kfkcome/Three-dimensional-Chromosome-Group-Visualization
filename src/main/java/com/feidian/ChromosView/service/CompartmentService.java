package com.feidian.ChromosView.service;

import com.feidian.ChromosView.domain.CompartmentPoint;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CompartmentService {
    int addCS();
    int addPointFromFile(String path);
    List<CompartmentPoint> findPointByCS_ID(int id);
}
