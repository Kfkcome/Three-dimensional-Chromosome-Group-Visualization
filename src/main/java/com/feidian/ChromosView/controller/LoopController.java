package com.feidian.ChromosView.controller;

import com.feidian.ChromosView.domain.LoopPointMB;
import com.feidian.ChromosView.exception.QueryException;
import com.feidian.ChromosView.log.LogPrint;
import com.feidian.ChromosView.service.LoopService;
import com.feidian.ChromosView.utils.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/loop")
public class LoopController {
    private final LoopService loopService;

    @Autowired
    public LoopController(LoopService loopService) {
        this.loopService = loopService;
    }

    //    @GetMapping("/cs_id/{cs_id}")
//    public ApiResponse findAllPoint(@PathVariable int cs_id) throws QueryException {
//        List<LoopPoint> allPoint = loopService.findAllPoint(cs_id);
//        if(allPoint.isEmpty())throw new QueryException("查询失败");
//        return ApiResponse.success(allPoint);
//    }
    @LogPrint
    @GetMapping("/cs_id/{cs_id}")
    public ApiResponse findPointByStart_End(@PathVariable int cs_id, @RequestParam(value = "end", required = false) String end, @RequestParam(value = "start", required = false) String start) throws QueryException {
        List<LoopPointMB> pointByStartEnd = loopService.findPointByStart_End(cs_id, start, end);
        if (pointByStartEnd.isEmpty()) throw new QueryException("查询失败");
        return ApiResponse.success(pointByStartEnd);
    }

    @LogPrint
    @GetMapping("/DoubleRange/cs_id/{cs_id}")
    public ApiResponse findPointByDouble(@PathVariable int cs_id, String start1, String end1, String start2, String end2) throws QueryException {
        List<LoopPointMB> pointByDoublePoint = loopService.findPointByDoublePoint(cs_id, start1, end1, start2, end2);
        if (pointByDoublePoint.isEmpty()) throw new QueryException("查询失败");
        return ApiResponse.success(pointByDoublePoint);
    }
}
