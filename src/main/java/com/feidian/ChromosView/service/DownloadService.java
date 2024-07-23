package com.feidian.ChromosView.service;

import com.feidian.ChromosView.utils.ApiResponse;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface DownloadService {
    ApiResponse<String> download2DFile(HttpServletResponse response, String type, String software, String species, String cultivar, String tissue) throws IOException;

    ApiResponse<String> download1DFile(HttpServletResponse response, String type, String species, String cultivar, String tissue) throws IOException;

}
