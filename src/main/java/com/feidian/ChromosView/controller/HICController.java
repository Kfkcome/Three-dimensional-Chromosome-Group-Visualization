package com.feidian.ChromosView.controller;

import com.feidian.ChromosView.aop.LogPrint;
import com.feidian.ChromosView.domain.Annotation2DPoint;
import com.feidian.ChromosView.domain.HicMapPoint;
import com.feidian.ChromosView.exception.HicFileNotFoundException;
import com.feidian.ChromosView.service.HicService;
import com.feidian.ChromosView.utils.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/hic")
public class HICController {
    private final HicService hicService;

    @Autowired
    public HICController(HicService hicService) {
        this.hicService = hicService;
    }


    @LogPrint
    @GetMapping("/generateHeatmap")
    public ApiResponse<String> generateHeatMap(@RequestParam(value = "species") String species, @RequestParam("cultivar") String cultivar, @RequestParam("tissue") String tissue, @RequestParam(value = "chromosome") String chromosome,
                                               @RequestParam(value = "DisplayOption", required = false) String displayOption, @RequestParam(value = "NormalizationType", required = false) String normalizationType,
                                               @RequestParam(value = "minColor", required = false) Double minColor, @RequestParam(value = "maxColor", required = false) Double maxColor,
                                               @RequestParam(value = "clarity", required = false) Integer clarity, HttpServletResponse httpServletResponse) throws HicFileNotFoundException {
        if (normalizationType.equals("Balanced11")) {
            normalizationType = "Balanced++";
            //由于++会被替换成空格所以约定用11替代++
        }
        if (hicService.generateMap(species, cultivar, tissue, chromosome, displayOption, normalizationType, minColor, maxColor, clarity, httpServletResponse))
            return ApiResponse.success("Generate heatmap successfully!");
        return ApiResponse.fail(505, "Failed to generate the heatmap");
    }

    @LogPrint
    @GetMapping("/generateHeatmap2D")
    public ApiResponse<String> generateHeatMap2D(@RequestParam(value = "species") String species, @RequestParam("cultivar") String cultivar, @RequestParam("tissue") String tissue, @RequestParam(value = "chromosome") String chromosome,
                                                 @RequestParam(value = "minColor", required = false) Double minColor, @RequestParam(value = "maxColor") Double maxColor,
                                                 @RequestParam(value = "clarity", required = false) Integer clarity,
                                                 @RequestParam(value = "loop") Boolean loop, @RequestParam("tad") Boolean tad, @RequestParam("tadSoftware") String tadSoftware,
                                                 @RequestParam(value = "loopSoftware") String loopSoftware,
                                                 @RequestParam(value = "DisplayOption", required = false) String displayOption, @RequestParam(value = "NormalizationType", required = false) String normalizationType,
                                                 HttpServletResponse httpServletResponse) throws HicFileNotFoundException {
        if (normalizationType.equals("Balanced11")) {
            normalizationType = "Balanced++";
            //由于++会被替换成空格所以约定用11替代++
        }
        if (hicService.generateAnnotation2DMap(species, cultivar, tissue, chromosome, displayOption, normalizationType, minColor, maxColor, clarity, tad, loop, tadSoftware, loopSoftware, httpServletResponse))
            return ApiResponse.success("Generate heatmap successfully!");
        return ApiResponse.fail(505, "Failed to generate the heatmap");
    }

    @LogPrint
    @GetMapping("/getHeatMapPoint")
    public ApiResponse<HicMapPoint> getHeatMapPoint(@RequestParam(value = "species") String species, @RequestParam("cultivar") String cultivar, @RequestParam("tissue") String tissue, @RequestParam(value = "chromosome") String chromosome, @RequestParam(value = "x") int x, @RequestParam(value = "y") int y) throws HicFileNotFoundException {
        String point = hicService.getPoint(species, cultivar, tissue, chromosome, x, y);
        if (point.isEmpty()) {
            return ApiResponse.error(400, "请先选择染色体并生成HIC图");
        }
        return ApiResponse.success(new HicMapPoint(point));
    }

    @LogPrint
    @GetMapping("/getAnnotationPoint")
    public ApiResponse<Annotation2DPoint> getAnnotationPoint(@RequestParam(value = "species") String species, @RequestParam("cultivar") String cultivar, @RequestParam("tissue") String tissue, @RequestParam(value = "chromosome") String chromosome,
                                                             @RequestParam(value = "loop") Boolean loop, @RequestParam("tad") Boolean tad, @RequestParam("tadSoftware") String tadSoftware,
                                                             @RequestParam(value = "loopSoftware") String loopSoftware,
                                                             int x, int y) throws IOException {
        ArrayList<String> annotation2DPoint = hicService.getAnnotation2DPoint(species, cultivar, tissue, chromosome, loop, tad, tadSoftware, loopSoftware, x, y);
        if (annotation2DPoint.isEmpty()) {
            return ApiResponse.fail(505, "found nothing");
        }
        return ApiResponse.success(new Annotation2DPoint(annotation2DPoint));
    }

    @LogPrint
    @GetMapping("/getNormalizationType")
    public ApiResponse<List<String>> getNormalizationType(@RequestParam(value = "species") String species, @RequestParam("cultivar") String cultivar, @RequestParam("tissue") String tissue, @RequestParam(value = "chromosome") String chromosome) throws HicFileNotFoundException {
        List<String> normalizationType = hicService.getNormalizationType(species, cultivar, tissue, chromosome);
        if (normalizationType.isEmpty()) {
            return ApiResponse.error(400, "请先选择染色体并生成HIC图");
        }
        return ApiResponse.success(normalizationType);
    }

    @LogPrint
    @GetMapping("/getDisplayOption")
    public ApiResponse<List<String>> getDisplayOption(@RequestParam(value = "species") String species, @RequestParam("cultivar") String cultivar, @RequestParam("tissue") String tissue, @RequestParam(value = "chromosome") String chromosome) throws HicFileNotFoundException {
        List<String> normalizationType = hicService.getDisplayOption(species, cultivar, tissue, chromosome);
        if (normalizationType.isEmpty()) {
            return ApiResponse.error(400, "请先选择染色体并生成HIC图");
        }
        return ApiResponse.success(normalizationType);
    }

}
