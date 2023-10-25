package com.feidian.ChromosView.controller;

import com.feidian.ChromosView.aop.LogPrint;
import com.feidian.ChromosView.domain.UUID_matrixPoints;
import com.feidian.ChromosView.exception.HicFileNotFoundException;
import com.feidian.ChromosView.exception.QueryException;
import com.feidian.ChromosView.service.HicService;
import com.feidian.ChromosView.utils.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;

@RestController
@RequestMapping("/hic")
public class HICController {
    private final HicService hicService;

    @Autowired
    public HICController(HicService hicService) {
        this.hicService = hicService;
    }

    @LogPrint()
    @GetMapping("/cs_id1/{cs_id1}/cs_id2/{cs_id2}/tissue_id/{tissue_id}")
    public ApiResponse<UUID_matrixPoints> findHICPoint(String uuid, @PathVariable Integer cs_id1, @PathVariable Integer cs_id2, @RequestParam(value = "norms", required = false) String norms,
                                                       @RequestParam(value = "binXStart", required = false) String binXStart, @RequestParam(value = "binYStart", required = false) String binYStart,
                                                       @RequestParam(value = "binXEnd", required = false) String binXEnd, @RequestParam(value = "binYEnd", required = false) String binYEnd,
                                                       @RequestParam(value = "resolution", required = false) String resolution, @PathVariable Integer tissue_id) throws QueryException, FileNotFoundException {
        UUID_matrixPoints byCSId = hicService.findByCS_ID(uuid, cs_id1, cs_id2, norms, binXStart, binYStart, binXEnd, binYEnd, resolution, tissue_id);
        if (byCSId.getUuid().equals("none"))
            return ApiResponse.success(202, byCSId);
        else if (!byCSId.getMatrixPoints().isEmpty())
            return ApiResponse.success(byCSId);
        else return ApiResponse.fail(ApiResponse.fail().getCode(), "查询不到数据");
    }

    @LogPrint
    @GetMapping("/generateHeatmap")
    public ApiResponse<String> generateHeatMap(@RequestParam(value = "species") String species, @RequestParam("cultivar") String cultivar, @RequestParam("tissue") String tissue, @RequestParam(value = "chromosome") String chromosome, HttpServletResponse httpServletResponse) throws HicFileNotFoundException {
        if (hicService.generateMap(species, cultivar, tissue, chromosome, httpServletResponse))
            return ApiResponse.success("Generate heatmap successfully!");
        return ApiResponse.fail(505, "Failed to generate the heatmap");
    }
}
