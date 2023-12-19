package com.feidian.ChromosView.controller;

import com.feidian.ChromosView.aop.LogPrint;
import com.feidian.ChromosView.domain.Annotation2DPoint;
import com.feidian.ChromosView.domain.HicMapPoint;
import com.feidian.ChromosView.domain.UUID_matrixPoints;
import com.feidian.ChromosView.exception.HicFileNotFoundException;
import com.feidian.ChromosView.exception.QueryException;
import com.feidian.ChromosView.service.HicService;
import com.feidian.ChromosView.utils.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
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
    public ApiResponse<String> generateHeatMap(@RequestParam(value = "species") String species, @RequestParam("cultivar") String cultivar, @RequestParam("tissue") String tissue, @RequestParam(value = "chromosome") String chromosome,
                                               @RequestParam(value = "DisplayOption", required = false) String displayOption, @RequestParam(value = "NormalizationType", required = false) String normalizationType,
                                               HttpServletResponse httpServletResponse) throws HicFileNotFoundException {
        if (normalizationType.equals("Balanced11")) {
            normalizationType = "Balanced++";
            //由于++会被替换成空格所以约定用11替代++
        }
        if (hicService.generateMap(species, cultivar, tissue, chromosome, displayOption, normalizationType, httpServletResponse))
            return ApiResponse.success("Generate heatmap successfully!");
        return ApiResponse.fail(505, "Failed to generate the heatmap");
    }

    @LogPrint
    @GetMapping("/generateHeatmap2D")
    public ApiResponse<String> generateHeatMap2D(@RequestParam(value = "species") String species, @RequestParam("cultivar") String cultivar, @RequestParam("tissue") String tissue, @RequestParam(value = "chromosome") String chromosome,
                                                 @RequestParam(value = "loop") Boolean loop, @RequestParam("tad") Boolean tad, @RequestParam("tadSoftware") String tadSoftware,
                                                 @RequestParam(value = "loopSoftware") String loopSoftware,
                                                 @RequestParam(value = "DisplayOption", required = false) String displayOption, @RequestParam(value = "NormalizationType", required = false) String normalizationType,
                                                 HttpServletResponse httpServletResponse) throws HicFileNotFoundException {
        if (normalizationType.equals("Balanced11")) {
            normalizationType = "Balanced++";
            //由于++会被替换成空格所以约定用11替代++
        }
        if (hicService.generateAnnotation2DMap(species, cultivar, tissue, chromosome, displayOption, normalizationType, tad, loop, tadSoftware, loopSoftware, httpServletResponse))
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
