package com.feidian.ChromosView.controller;

import com.feidian.ChromosView.aop.LogPrint;
import com.feidian.ChromosView.service.DownloadService;
import com.feidian.ChromosView.utils.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/download")
public class DownloadController {
    DownloadService downloadService;

    @Autowired
    DownloadController(DownloadService downloadService) {
        this.downloadService = downloadService;
    }

    @LogPrint
    @GetMapping("/2DAnnotation")
    public ApiResponse<String> download2DFile(String type, String software, String species, String cultivar, String tissue, HttpServletResponse response) {
        try {
            return downloadService.download2DFile(response, type, software, species, cultivar, tissue);
        } catch (IOException e) {
            e.printStackTrace();
            return ApiResponse.fail(502, "下载失败");
        }
    }

    @LogPrint
    @GetMapping("/1DAnnotation")
    public ApiResponse<String> download1DFile(String type, String species, String cultivar, String tissue, HttpServletResponse response) {
        try {
            return downloadService.download1DFile(response, type, species, cultivar, tissue);
        } catch (IOException e) {
            e.printStackTrace();
            return ApiResponse.fail(502, "下载失败");
        }
    }

}
