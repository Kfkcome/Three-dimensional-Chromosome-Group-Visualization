package com.feidian.ChromosView.service.impl;

import com.feidian.ChromosView.domain.CompartmentPointMB;
import com.feidian.ChromosView.domain.LoopPointMB;
import com.feidian.ChromosView.service.FileService;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
@Service
public class FileServiceImpl implements FileService {
    @Override
    public void getCompartmentFile(HttpServletResponse response, ArrayList<CompartmentPointMB> compartmentPointMBS) {
        // 读到流中
        //response.reset();
        //TODO：修复使用原坐标数据而不是使用相对坐标
        response.setContentType("application/octet-stream");
        String filename = "compartment.txt";
        try {
            response.addHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(filename, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        ServletOutputStream outputStream = null;
        try {
            outputStream = response.getOutputStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            outputStream.write(("ID"+"\t"+"CS_ID"+"\t"+"START_POINT"+'\t'+"END_POINT"+'\t'+"VALUE"+"\n").getBytes());
            for (CompartmentPointMB compartmentPointMB : compartmentPointMBS) {
                byte[] bytes = compartmentPointMB.toString().getBytes();

                    outputStream.write(bytes);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void getLoopFile(HttpServletResponse response,List<LoopPointMB> loopPointMBS) {
        //response.reset();
        // TODO：修复使用原坐标数据而不是使用相对坐标
        response.setContentType("application/octet-stream");
        String filename = "loop.txt";
        try {
            response.addHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(filename, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        ServletOutputStream outputStream = null;
        try {
            outputStream = response.getOutputStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            outputStream.write(("IC_ID"+"\t"+"CS_ID"+"\t"+"START_POINT"+"\t"+"END_POINT"+"\t"+"IC_NUM"+"\t"+"FDR_VALUE"+"\n").getBytes());
            for (LoopPointMB loopPointMB : loopPointMBS) {
                byte[] bytes = loopPointMB.toString().getBytes();
                outputStream.write(bytes);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
