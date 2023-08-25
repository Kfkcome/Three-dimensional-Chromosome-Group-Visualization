package com.feidian.ChromosView.service;

import com.feidian.ChromosView.domain.CompartmentPointMB;
import com.feidian.ChromosView.domain.LoopPointMB;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

public interface FileService {
    void getCompartmentFile(HttpServletResponse response, ArrayList<CompartmentPointMB> compartmentPointMBS);
    void getLoopFile(HttpServletResponse response,List<LoopPointMB> loopPointMBS);
}
