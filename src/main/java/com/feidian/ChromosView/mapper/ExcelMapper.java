package com.feidian.ChromosView.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

@Mapper
@Repository
public interface ExcelMapper {
    List<HashMap<String, String>> getExcelTableComparation(@Param("pageNow") int pageNow, @Param("pageSize") int pageSize);

    List<HashMap<String, String>> getExcelTableGenomics(@Param("pageNow") int pageNow, @Param("pageSize") int pageSize);

    List<HashMap<String, String>> getExcelTableHiCMatrix(@Param("pageNow") int pageNow, @Param("pageSize") int pageSize);

    List<HashMap<String, String>> getExcelTableLoop(@Param("pageNow") int pageNow, @Param("pageSize") int pageSize);

    List<HashMap<String, String>> getExcelTableTad(@Param("pageNow") int pageNow, @Param("pageSize") int pageSize);

    List<HashMap<String, String>> getExcelTableCompartment(@Param("pageNow") int pageNow, @Param("pageSize") int pageSize);

    List<HashMap<String, String>> getExcelDataOverview(@Param("pageNow") int pageNow, @Param("pageSize") int pageSize);

    Integer getExcelTableCount(@Param("table") String table);

    List<HashMap<String, String>> searchExcelTableComparation(@Param("searchParam") String params, @Param("pageNow") int pageNow, @Param("pageSize") int pageSize);

    List<HashMap<String, String>> searchExcelTableGenomics(@Param("searchParam") String params, @Param("pageNow") int pageNow, @Param("pageSize") int pageSize);

    List<HashMap<String, String>> searchExcelTableHiCMatrix(@Param("searchParam") String params, @Param("pageNow") int pageNow, @Param("pageSize") int pageSize);

    List<HashMap<String, String>> searchExcelTableLoop(@Param("searchParam") String params, @Param("pageNow") int pageNow, @Param("pageSize") int pageSize);

    List<HashMap<String, String>> searchExcelTableTad(@Param("searchParam") String params, @Param("pageNow") int pageNow, @Param("pageSize") int pageSize);

    List<HashMap<String, String>> searchExcelTableCompartment(@Param("searchParam") String params, @Param("pageNow") int pageNow, @Param("pageSize") int pageSize);

    List<HashMap<String, String>> searchExcelDataOverview(@Param("searchParam") String params, @Param("pageNow") int pageNow, @Param("pageSize") int pageSize);

}
