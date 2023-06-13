package com.feidian.ChromosView.controller;

import com.feidian.ChromosView.domain.LoopPoint;
import com.feidian.ChromosView.exception.QueryException;
import com.feidian.ChromosView.service.LoopService;
import com.feidian.ChromosView.utils.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/loop")
public class LoopController {
    private final LoopService loopService;
    @Autowired
    public LoopController(LoopService loopService) {
        this.loopService = loopService;
    }
    @GetMapping("/cs_id/{cs_id}")
    public ApiResponse findAllPoint(@PathVariable int cs_id) throws QueryException {
        List<LoopPoint> allPoint = loopService.findAllPoint(cs_id);
        if(allPoint.isEmpty())throw new QueryException("查询失败");
        return ApiResponse.success(allPoint);
    }
    @GetMapping("/cs_id/{cs_id}/start/{start}/end/{end}")
    public ApiResponse findPointByStart_End(@PathVariable int cs_id, @PathVariable Long end, @PathVariable Long start) throws QueryException {
        List<LoopPoint> pointByStartEnd = loopService.findPointByStart_End(cs_id, start, end);
        if(pointByStartEnd.isEmpty())throw new QueryException("查询失败");
        return ApiResponse.success(pointByStartEnd);
    }
}
