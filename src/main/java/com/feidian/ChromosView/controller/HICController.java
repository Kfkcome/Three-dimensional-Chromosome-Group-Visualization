package com.feidian.ChromosView.controller;

import com.feidian.ChromosView.domain.MatrixPoint;
import com.feidian.ChromosView.exception.QueryException;
import com.feidian.ChromosView.service.HicService;
import com.feidian.ChromosView.utils.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/hic")
public class HICController {
    private final HicService hicService;
    @Autowired
    public HICController(HicService hicService) {
        this.hicService = hicService;
    }
    @GetMapping("/cs_id/{cs_id}")
    public ApiResponse findHICPoint(@PathVariable Integer cs_id, @RequestParam(value = "norms",required = false)String norms,
                                    @RequestParam(value = "binXStart",required = false) String binXStart,@RequestParam(value = "binYStart",required = false) String binYStart,
                                    @RequestParam(value = "binXEnd",required = false) String binXEnd,@RequestParam(value = "binYEnd",required = false)String binYEnd) throws QueryException {
        List<MatrixPoint> byCSId = hicService.findByCS_ID(cs_id,norms,binXStart,binYStart,binXEnd,binYEnd);
        if(!byCSId.isEmpty())
            return ApiResponse.success(byCSId);
        else return ApiResponse.fail(ApiResponse.fail().getCode(), "查询不到数据");
    }
}
