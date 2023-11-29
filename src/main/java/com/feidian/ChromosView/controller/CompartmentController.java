package com.feidian.ChromosView.controller;

import com.feidian.ChromosView.aop.LogPrint;
import com.feidian.ChromosView.domain.CompartmentPoint;
import com.feidian.ChromosView.domain.CompartmentPointData;
import com.feidian.ChromosView.domain.CompartmentPointMB;
import com.feidian.ChromosView.exception.QueryException;
import com.feidian.ChromosView.service.CompartmentService;
import com.feidian.ChromosView.service.FileService;
import com.feidian.ChromosView.utils.ApiResponse;
import com.feidian.ChromosView.utils.UnitConversion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/compartment")
public class CompartmentController {
    private final CompartmentService compartmentService;
    private final FileService fileService;

    @Autowired
    public CompartmentController(CompartmentService compartmentService, FileService fileService) {
        this.compartmentService = compartmentService;
        this.fileService = fileService;
    }

    @LogPrint
    @GetMapping("/cs_id/{cs_id}/tissue_id/{tissue_id}/software/{software_id}")
    ApiResponse<ArrayList<CompartmentPointMB>> findPointBYStart_End(@PathVariable int cs_id, @RequestParam(name = "start", required = false) String start, @RequestParam(name = "end", required = false) String end,
                                                                    @PathVariable Integer tissue_id, @PathVariable Integer software_id) throws QueryException {
        List<CompartmentPoint> pointByENDStart = compartmentService.findPointByEND_START(cs_id, start, end, tissue_id, software_id);
        if (pointByENDStart.isEmpty()) {
            throw new QueryException("查询不到数据");
        }
        ArrayList<CompartmentPointMB> convert = UnitConversion.convert((ArrayList<CompartmentPoint>) pointByENDStart);
        return ApiResponse.success(convert);
    }

    @LogPrint
    @GetMapping("/file/cs_id/{cs_id}/tissue_id/{tissue_id}/software/{software_id}")
    void getFileBYStart_End(HttpServletResponse response, @PathVariable int cs_id, @RequestParam(name = "start", required = false) String start, @RequestParam(name = "end", required = false) String end,
                            @PathVariable Integer tissue_id, @PathVariable Integer software_id) throws QueryException {
        List<CompartmentPoint> pointByENDStart = compartmentService.findPointByEND_START(cs_id, start, end, tissue_id, software_id);
        if (pointByENDStart.isEmpty()) {
            throw new QueryException("查询不到数据");
        }
        fileService.getCompartmentFile(response, pointByENDStart);
    }

    @LogPrint
    @GetMapping("/generate")
    ApiResponse<String> generateCompartment(@RequestParam(value = "species") String species, @RequestParam("cultivar") String cultivar, @RequestParam("tissue") String tissue, @RequestParam(value = "chromosome") String chromosome,
                                            HttpServletResponse httpServletResponse) {
        if (compartmentService.generateCompartment(species, cultivar, tissue, chromosome, httpServletResponse)) {
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
