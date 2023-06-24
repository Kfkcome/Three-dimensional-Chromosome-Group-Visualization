package com.feidian.ChromosView.controller;

import com.feidian.ChromosView.domain.RNA;
import com.feidian.ChromosView.domain.RNA_LIST;
import com.feidian.ChromosView.domain.RNA_STRUCTURE_T;
import com.feidian.ChromosView.log.LogPrint;
import com.feidian.ChromosView.service.RnaService;
import com.feidian.ChromosView.utils.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/RNA")
public class RnaController {
    private final RnaService rnaService;

    @Autowired
    public RnaController(RnaService rnaService) {
        this.rnaService = rnaService;
    }

    @LogPrint
    @GetMapping("/cs_id/{cs_id}")
    ApiResponse<RNA_LIST> findRNAByCS_ID(@PathVariable Integer cs_id, String start, String end) {
        List<RNA> rnaByStartEND = rnaService.findRnaByStartEND(cs_id, start, end);
        List<RNA_STRUCTURE_T> rnaByStartEND1 = rnaService.findRnaByStartEND(rnaByStartEND);
        RNA_LIST rnaList = new RNA_LIST(rnaByStartEND, rnaByStartEND1);
        if (rnaList.getRnaStructureTs().isEmpty()) {
            return ApiResponse.fail(ApiResponse.fail().getCode(), "查询失败");
        }
        return ApiResponse.success(rnaList);
    }
}
