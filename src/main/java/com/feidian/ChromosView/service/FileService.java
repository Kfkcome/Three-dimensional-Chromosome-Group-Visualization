package com.feidian.ChromosView.service;

import com.feidian.ChromosView.domain.CompartmentPoint;
import com.feidian.ChromosView.domain.LoopPoint;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface FileService {
    void getCompartmentFile(HttpServletResponse response, List<CompartmentPoint> compartmentPointMBS);

    void getLoopFile(HttpServletResponse response, List<LoopPoint> loopPointS);
}
