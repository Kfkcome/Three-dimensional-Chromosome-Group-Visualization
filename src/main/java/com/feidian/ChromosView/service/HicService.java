package com.feidian.ChromosView.service;

import com.feidian.ChromosView.domain.UUID_matrixPoints;
import com.feidian.ChromosView.exception.QueryException;

public interface HicService {
    public UUID_matrixPoints findByCS_ID(String uuid, int cs_id1, int cs_id2, String norms, String binXStart, String binYStart, String binXEnd, String binYEnd, String resolution) throws QueryException;
}
