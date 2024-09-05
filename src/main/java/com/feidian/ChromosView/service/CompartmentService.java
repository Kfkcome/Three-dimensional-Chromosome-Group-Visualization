package com.feidian.ChromosView.service;

import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;

@Service
public interface CompartmentService {

    Boolean generateCompartment(String species, String cultivar, String tissue, String chromosome, Integer clarity, HttpServletResponse response);

    String getPointData(String species, String cultivar, String tissue, String chromosome, int x, int y);

}
