package com.feidian.ChromosView.controller;

import com.feidian.ChromosView.aop.LogPrint;
import com.feidian.ChromosView.domain.*;
import com.feidian.ChromosView.exception.HicFileNotFoundException;
import com.feidian.ChromosView.service.RnaService;
import com.feidian.ChromosView.utils.ApiResponse;
import com.feidian.ChromosView.utils.UnitConversion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
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
    ApiResponse<RNA_LIST> findRNAByCS_ID(@PathVariable Integer cs_id, String start, String end, String singleLine) {
        List<RNA> rnaByStartEND = rnaService.findRnaByStartEND(cs_id, start, end, singleLine);
        //添加不同层数的功能
        List<List<RNA>> rnaSplit = new ArrayList<>();
        for (RNA rna : rnaByStartEND) {
            int count = 0;
            for (RNA rna1 : rnaByStartEND) {
                if (rna != rna1 && rna1.getSTART_POINT() < rna.getEND_POINT() && rna1.getEND_POINT() > rna.getSTART_POINT()) {
                    count++;
                }
            }
            if (rnaSplit.size() > count + 1) {
                rnaSplit.get(count).add(rna);
            } else {
                for (int i = 0; i <= count; i++) {
                    try {
                        rnaSplit.get(i);
                    } catch (IndexOutOfBoundsException e) {
                        rnaSplit.add(new ArrayList<RNA>());//增加层数
                    }
                }
                rnaSplit.get(count).add(rna);
            }
        }
        List<List<RNA_STRUCTURE_T>> rnaStructSplit = new ArrayList<>();
        for (List<RNA> rnas : rnaSplit) {
            List<RNA_STRUCTURE_T> rnaByStartEND1 = rnaService.findRnaByStartEND(rnas);
            rnaStructSplit.add(rnaByStartEND1);
        }

        //转换单位
        List<List<RNA_T>> rnaSplit2 = new ArrayList<>();
        for (List<RNA> rnas : rnaSplit) {
            rnaSplit2.add(UnitConversion.convertRNA(rnas));
        }
        RNA_LIST rnaList = new RNA_LIST(rnaSplit2, rnaStructSplit);
        if (rnaList.getRnaStructureTs().isEmpty()) {
            return ApiResponse.fail(ApiResponse.fail().getCode(), "查询失败");
        }
        return ApiResponse.success(rnaList);
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
