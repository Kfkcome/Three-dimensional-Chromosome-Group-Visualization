package com.feidian.ChromosView.service;

import com.feidian.ChromosView.domain.MatrixPoint;

import java.util.List;

public interface HicService {
    public List<MatrixPoint> findByCS_ID();
}
