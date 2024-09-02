package com.feidian.ChromosView.service.impl;

import com.feidian.ChromosView.exception.QueryException;
import com.feidian.ChromosView.mapper.ExcelMapper;
import com.feidian.ChromosView.service.ExcelService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class ExcelServiceImpl<T> implements ExcelService<T> {
    final ExcelMapper excelMapper;
    HashMap<String, String> domain_to_table;

    public ExcelServiceImpl(ExcelMapper excelMapper) {
        this.excelMapper = excelMapper;
        domain_to_table = new HashMap<>();
        domain_to_table.put("excel_comparation", "ExcelComparation");
        domain_to_table.put("excel_compartment", "ExcelCompartment");
        domain_to_table.put("excel_genomics", "ExcelGenomics");
        domain_to_table.put("excel_hic_matrix", "ExcelHiCMatrix");
        domain_to_table.put("excel_loop", "ExcelLoop");
        domain_to_table.put("excel_tad", "ExcelTad");
    }

    private List<? extends Object> getExcelDataFromDB(String tableName, Integer pageNow, Integer pageSize) throws QueryException {


        String s = domain_to_table.get(tableName);
        if (s == null) {
            throw new RuntimeException("你输入的表不存在");
        } else if (s.equals("ExcelComparation")) {
            return excelMapper.getExcelTableComparation(pageNow, pageSize);
        } else if (s.equals("ExcelCompartment")) {
            return excelMapper.getExcelTableCompartment(pageNow, pageSize);
        } else if (s.equals("ExcelGenomics")) {
            return excelMapper.getExcelTableGenomics(pageNow, pageSize);
        } else if (s.equals("ExcelHiCMatrix")) {
            return excelMapper.getExcelTableHiCMatrix(pageNow, pageSize);
        } else if (s.equals("ExcelLoop")) {
            return excelMapper.getExcelTableLoop(pageNow, pageSize);
        } else if (s.equals("ExcelTad")) {
            return excelMapper.getExcelTableTad(pageNow, pageSize);
        } else {
            throw new QueryException("你输入的表不存在");
        }
    }

    private List<? extends Object> searchExcelDataFromDB(String tableName, String searchParam, Integer pageNow, Integer pageSize) throws QueryException {
        String s = domain_to_table.get(tableName);
        if (s == null) {
            throw new QueryException("你输入的表不存在");
        } else if (s.equals("ExcelComparation")) {
            return excelMapper.searchExcelTableComparation(searchParam, pageNow, pageSize);
        } else if (s.equals("ExcelCompartment")) {
            return excelMapper.searchExcelTableCompartment(searchParam, pageNow, pageSize);
        } else if (s.equals("ExcelGenomics")) {
            return excelMapper.searchExcelTableGenomics(searchParam, pageNow, pageSize);
        } else if (s.equals("ExcelHiCMatrix")) {
            return excelMapper.searchExcelTableHiCMatrix(searchParam, pageNow, pageSize);
        } else if (s.equals("ExcelLoop")) {
            return excelMapper.searchExcelTableLoop(searchParam, pageNow, pageSize);
        } else if (s.equals("ExcelTad")) {
            return excelMapper.searchExcelTableTad(searchParam, pageNow, pageSize);
        } else {
            throw new QueryException("你输入的表不存在");
        }
    }

    @Override
    public List<Object> getExcelData(String tableName, Integer pageNow, Integer pageSize) throws QueryException {
        List<Object> domain_list = new ArrayList<>();
        domain_list.addAll(getExcelDataFromDB(tableName, pageNow, pageSize));
        return domain_list;
    }

    @Override
    public List<Object> searchExcelData(String tableName, String searchParam, Integer pageNow, Integer pageSize) throws QueryException {
        ArrayList<Object> domain_list = new ArrayList<>();
        domain_list.addAll(searchExcelDataFromDB(tableName, searchParam, pageNow, pageSize));
        return domain_list;
    }

    @Override
    public Integer getExcelDataCount(String tableName) throws QueryException {
        String s = domain_to_table.get(tableName);
        if (s == null) {
            throw new QueryException("你输入的表不存在");
        } else {
            return excelMapper.getExcelTableCount(tableName);
        }
    }
}
