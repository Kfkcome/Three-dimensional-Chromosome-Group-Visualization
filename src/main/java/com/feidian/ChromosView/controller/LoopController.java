package com.feidian.ChromosView.controller;

import com.feidian.ChromosView.service.LoopService;
import com.feidian.ChromosView.utils.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/loop")
public class LoopController {
    private final LoopService loopService;
    @Autowired
    public LoopController(LoopService loopService) {
        this.loopService = loopService;
    }
    @GetMapping("/all/{cs_id}")
    public ApiResponse findAllPoint(@PathVariable int cs_id){
        loopService.findAllPoint(cs_id);
        return ApiResponse.success();
    }
}
