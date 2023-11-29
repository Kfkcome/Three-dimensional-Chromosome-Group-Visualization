package com.feidian.ChromosView.service;

import com.feidian.ChromosView.domain.CompartmentPoint;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Service
public interface CompartmentService {
    int addCS();

    int addPointFromFile(String path);

    List<CompartmentPoint> findPointByCS_ID(int id, int tissue_id, int software_id);

    List<CompartmentPoint> findPointByEND_START(int cs_id, String startT, String endT, int tissue_id, int software_id);

    Boolean generateCompartment(String species, String cultivar, String tissue, String chromosome, HttpServletResponse response);

    String getPointData(String species, String cultivar, String tissue, String chromosome, int x, int y);

}
