package com.feidian.ChromosView.service;

import org.springframework.stereotype.Service;

@Service
public interface CompartmentService {
    int addCS();
    int addPointFromFile(String path);
}
