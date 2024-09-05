package com.feidian.ChromosView.controller;

import com.feidian.ChromosView.aop.LogPrint;
import com.feidian.ChromosView.domain.GeneStructPoint;
import com.feidian.ChromosView.exception.HicFileNotFoundException;
import com.feidian.ChromosView.service.RnaService;
import com.feidian.ChromosView.utils.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/RNA")
public class RnaController {
    private final RnaService rnaService;

    @Autowired
    public RnaController(RnaService rnaService) {
        this.rnaService = rnaService;
    }


    @LogPrint
    @GetMapping("/RnaStruct")
    public ApiResponse<String> generateRnaStruct(@RequestParam(value = "species") String species, @RequestParam("cultivar") String cultivar, @RequestParam("tissue") String tissue, @RequestParam(value = "chromosome") String chromosome,
                                                 @RequestParam(value = "clarity", required = false) Integer clarity,
                                                 HttpServletResponse httpServletResponse) throws HicFileNotFoundException, IOException {
        if (rnaService.generateRnaStruct(species, cultivar, tissue, chromosome, clarity, httpServletResponse))
            return ApiResponse.success("Generate gene struct successfully!");
        return ApiResponse.fail(505, "Failed to generate the gene struct map");
    }

    @LogPrint
    @GetMapping("/RnaStructPoint")
    public ApiResponse<GeneStructPoint> getRnaStructPoint(@RequestParam(value = "species") String species, @RequestParam("cultivar") String cultivar,
                                                          @RequestParam("tissue") String tissue, @RequestParam(value = "chromosome") String chromosome, @RequestParam(value = "x") int x, @RequestParam(value = "y") int y) {
        String pointData = rnaService.getPointData(species, cultivar, tissue, chromosome, x, y);
        if (pointData == null || pointData.isEmpty()) {
            return ApiResponse.error(400, "找不到数据");
        }
        return ApiResponse.success(new GeneStructPoint(pointData));
    }
}
