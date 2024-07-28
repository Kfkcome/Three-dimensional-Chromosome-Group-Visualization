package com.feidian.ChromosView.service;

import com.feidian.ChromosView.exception.QueryException;

import java.util.List;

public interface ExcelService<T> {
    List<Object> getExcelData(String tableName, Integer pageNow, Integer pageSize) throws QueryException;

    Integer getExcelDataCount(String tableName) throws QueryException;
}
