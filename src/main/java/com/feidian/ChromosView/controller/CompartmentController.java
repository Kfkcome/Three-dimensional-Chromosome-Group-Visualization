package com.feidian.ChromosView.controller;

import com.feidian.ChromosView.domain.CompartmentPoint;
import com.feidian.ChromosView.domain.CompartmentPointMB;
import com.feidian.ChromosView.exception.QueryException;
import com.feidian.ChromosView.log.LogPrint;
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
        List<CompartmentPoint> pointByENDStart = compartmentService.findPointByEND_START(cs_id, start, end,tissue_id,software_id);
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
        List<CompartmentPoint> pointByENDStart = compartmentService.findPointByEND_START(cs_id, start, end,tissue_id,software_id);
        if (pointByENDStart.isEmpty()) {
            throw new QueryException("查询不到数据");
        }
        ArrayList<CompartmentPointMB> convert = UnitConversion.convert((ArrayList<CompartmentPoint>) pointByENDStart);
        fileService.getCompartmentFile(response,convert);
    }
}
