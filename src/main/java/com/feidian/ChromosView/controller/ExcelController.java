package com.feidian.ChromosView.controller;

import com.feidian.ChromosView.aop.LogPrint;
import com.feidian.ChromosView.exception.QueryException;
import com.feidian.ChromosView.service.ExcelService;
import com.feidian.ChromosView.utils.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/excel")
public class ExcelController {
    ExcelService excelService;

    @Autowired
    public ExcelController(ExcelService excelService) {
        this.excelService = excelService;
    }

    @LogPrint
    @GetMapping("/data")
    ApiResponse<List<Object>> getExcelData(String tableName, Integer pageNow, Integer pageSize) {
        try {
            return ApiResponse.success(excelService.getExcelData(tableName, pageNow, pageSize));
        } catch (QueryException e) {
            return ApiResponse.fail(500, e.getMessage());
        }
    }

    @LogPrint
    @GetMapping("/search")
    ApiResponse<List<Object>> searchExcelData(String tableName, String searchParam, Integer pageNow, Integer pageSize) {
        try {
            return ApiResponse.success(excelService.searchExcelData(tableName, searchParam, pageNow, pageSize));
        } catch (QueryException e) {
            return ApiResponse.fail(500, e.getMessage());
        }
    }

    @LogPrint
    @GetMapping("/count")
    ApiResponse<Integer> getExcelDataCount(String tableName) {
        try {
            return ApiResponse.success(excelService.getExcelDataCount(tableName));
        } catch (QueryException e) {
            return ApiResponse.fail(500, e.getMessage());
        }
    }
}
