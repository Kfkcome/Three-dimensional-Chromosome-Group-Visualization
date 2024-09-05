package com.feidian.ChromosView.controller;

import com.feidian.ChromosView.aop.LogPrint;
import com.feidian.ChromosView.domain.CompartmentPointData;
import com.feidian.ChromosView.service.CompartmentService;
import com.feidian.ChromosView.utils.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/compartment")
public class CompartmentController {
    private final CompartmentService compartmentService;

    @Autowired
    public CompartmentController(CompartmentService compartmentService) {
        this.compartmentService = compartmentService;
    }


    @LogPrint
    @GetMapping("/generate")
    ApiResponse<String> generateCompartment(@RequestParam(value = "species") String species, @RequestParam("cultivar") String cultivar,
                                            @RequestParam("tissue") String tissue, @RequestParam(value = "chromosome") String chromosome,
                                            @RequestParam(value = "clarity", required = false) Integer clarity,
                                            HttpServletResponse httpServletResponse) {
        if (compartmentService.generateCompartment(species, cultivar, tissue, chromosome, clarity, httpServletResponse)) {
            return ApiResponse.success("Generate Compartment successfully!");
        }
        return ApiResponse.fail(505, "Failed to generate the compartment");
    }

    @LogPrint
    @GetMapping("/point")
    public ApiResponse<CompartmentPointData> getRnaStructPoint(@RequestParam(value = "species") String species, @RequestParam("cultivar") String cultivar,
                                                               @RequestParam("tissue") String tissue, @RequestParam(value = "chromosome") String chromosome, @RequestParam(value = "x") int x, @RequestParam(value = "y") int y) {
        String pointData = compartmentService.getPointData(species, cultivar, tissue, chromosome, x, y);
        if (pointData == null || pointData.isEmpty()) {
            return ApiResponse.error(400, "找不到数据");
        }
        return ApiResponse.success(new CompartmentPointData(pointData));
    }
}
