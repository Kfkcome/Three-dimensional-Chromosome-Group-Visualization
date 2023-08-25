package com.feidian.ChromosView.service.impl;

import com.feidian.ChromosView.domain.CompartmentPoint;
import com.feidian.ChromosView.domain.LoopPoint;
import com.feidian.ChromosView.mapper.ChromosomeMapper;
import com.feidian.ChromosView.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

@Service
public class FileServiceImpl implements FileService {
    private final ChromosomeMapper chromosomeMapper;

    @Autowired
    public FileServiceImpl(ChromosomeMapper chromosomeMapper) {
        this.chromosomeMapper = chromosomeMapper;
    }

    @Override
    public void getCompartmentFile(HttpServletResponse response, List<CompartmentPoint> compartmentPointS) {
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
        String CS_name = chromosomeMapper.findByCS_ID(compartmentPointS.get(1).getCS_ID()).getCS_NAME();
        try {
            outputStream.write(("ChromosomeName" + "\t" + "START" + '\t' + "END" + '\t' + "VALUE" + "\n").getBytes());
            for (CompartmentPoint compartmentPoint : compartmentPointS) {
                byte[] bytes = (CS_name + "\t" + compartmentPoint.toString()).getBytes();
                outputStream.write(bytes);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void getLoopFile(HttpServletResponse response, List<LoopPoint> loopPointS) {
        //response.reset();
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
        String CS_name = chromosomeMapper.findByCS_ID(loopPointS.get(1).getCS_ID()).getCS_NAME();
        try {
            outputStream.write(("ChromosomeName" + "\t" + "Start" + "\t" + "End" + "\t" + "ChromosomeName" + "\t" + "Start"
                    + "\t" + "End" + "\t" + "IC_NUM" + "\t" + "FDR_Value" + "\n").getBytes());
            for (LoopPoint loopPoint : loopPointS) {
                byte[] bytes = loopPoint.toString(CS_name).getBytes();
                outputStream.write(bytes);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
