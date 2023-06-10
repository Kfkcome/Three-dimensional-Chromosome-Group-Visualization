package com.feidian.ChromosView.controller;

import com.feidian.ChromosView.domain.CompartmentPoint;
import com.feidian.ChromosView.domain.CompartmentPointMB;
import com.feidian.ChromosView.exception.QueryException;
import com.feidian.ChromosView.service.CompartmentService;
import com.feidian.ChromosView.utils.ApiResponse;
import com.feidian.ChromosView.utils.UnitConversion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/compartment")
public class CompartmentController {
    private final CompartmentService compartmentService;
    @Autowired
    public CompartmentController(CompartmentService compartmentService) {
        this.compartmentService = compartmentService;
    }
    @GetMapping("/cs_id/{id}")
    ApiResponse findPointById(@PathVariable int id) throws QueryException {
        List<CompartmentPoint> pointByCSId = compartmentService.findPointByCS_ID(id);
        if(pointByCSId.isEmpty())
        {
            throw new QueryException("查询不到数据");
        }
        ArrayList<CompartmentPointMB> convert = UnitConversion.convert((ArrayList<CompartmentPoint>) pointByCSId);
        return ApiResponse.success(convert);
    }
}
