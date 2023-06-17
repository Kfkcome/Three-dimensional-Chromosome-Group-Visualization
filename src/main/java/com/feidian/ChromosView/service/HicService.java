package com.feidian.ChromosView.service;

import com.feidian.ChromosView.domain.MatrixPoint;
import com.feidian.ChromosView.exception.QueryException;

import java.util.List;

public interface HicService {
    public List<MatrixPoint> findByCS_ID(int cs_id,String norms,String binXStart,String binYStart,String binXEnd,String binYEnd) throws QueryException;
}
