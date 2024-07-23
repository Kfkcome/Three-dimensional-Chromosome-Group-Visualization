package com.feidian.ChromosView.service.impl;

import com.feidian.ChromosView.service.DownloadService;
import com.feidian.ChromosView.utils.ApiResponse;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.file.Files;

@Service
public class DownloadServiceImpl implements DownloadService {

    private String uniteFileName(String species, String cultivar, String tissue) {
        return species + "_" + cultivar + "_" + tissue;
    }

    @Override
    public ApiResponse<String> download2DFile(HttpServletResponse response, String type, String software, String species, String cultivar, String tissue) throws IOException {
        String fileName = uniteFileName(species, cultivar, tissue);
        String filePath = "";
        if (type.equals("loop")) {
            filePath = "Loop/" + software + "/" + fileName + ".bedpe";
        }
        if (type.equals("TAD")) {
            filePath = "TAD/" + software + "/" + fileName + ".bedpe";
        }
        if (filePath.isEmpty())
            return ApiResponse.fail(402, "你选择的二维注释类型不存在");
        return fileDownloadUtils(response, filePath);
    }

    @Override
    public ApiResponse<String> download1DFile(HttpServletResponse response, String type, String species, String cultivar, String tissue) throws IOException {
        String fileName = uniteFileName(species, cultivar, tissue);
        String filePath = "";
        if (type.equals("gene")) {
            filePath = "Gene/" + fileName + ".bed.gz";
        }
        if (type.equals("Compartment")) {
            filePath = "Compartment/" + fileName + ".bw";
        }
        if (filePath.isEmpty())
            return ApiResponse.fail(402, "你选择的一维注释类型不存在");
        return fileDownloadUtils(response, filePath);
    }

    @NotNull
    private ApiResponse<String> fileDownloadUtils(HttpServletResponse response, String filePath) throws IOException {
        File file = new File(filePath);
        if (!file.exists())
            return ApiResponse.fail(402, "文件不存在，请检查物种、品种、组织是否正确");
        InputStream inputStream = null;
        ServletOutputStream ouputStream = null;
        try {
            inputStream = Files.newInputStream(file.toPath());
            response.setContentType("application/x-msdownload");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(file.getName(), "UTF-8"));
            // 设置一个总长度，否则无法估算进度
            response.setHeader("Content-Length", String.valueOf(file.length()));
            ouputStream = response.getOutputStream();
            byte b[] = new byte[1024];
            int n;
            while ((n = inputStream.read(b)) != -1) {
                ouputStream.write(b, 0, n);
            }
            ouputStream.flush();

        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.fail(502, "下载失败");

        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
            if (ouputStream != null) {
                ouputStream.close();
            }
        }

        return ApiResponse.success("下载成功");
    }

}
