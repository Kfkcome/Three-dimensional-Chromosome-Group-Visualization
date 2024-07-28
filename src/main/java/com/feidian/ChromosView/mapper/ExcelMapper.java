package com.feidian.ChromosView.mapper;

import com.feidian.ChromosView.domain.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface ExcelMapper {
    List<Object> getExcelTable(@Param("table") String table, @Param("pageNow") int pageNow, @Param("pageSize") int pageSize);

    List<ExcelComparation> getExcelTableComparation(@Param("pageNow") int pageNow, @Param("pageSize") int pageSize);

    List<ExcelGenomics> getExcelTableGenomics(@Param("pageNow") int pageNow, @Param("pageSize") int pageSize);

    List<ExcelHiCMatrix> getExcelTableHiCMatrix(@Param("pageNow") int pageNow, @Param("pageSize") int pageSize);

    List<ExcelLoop> getExcelTableLoop(@Param("pageNow") int pageNow, @Param("pageSize") int pageSize);

    List<ExcelTad> getExcelTableTad(@Param("pageNow") int pageNow, @Param("pageSize") int pageSize);

    Integer getExcelTableCount(@Param("table") String table);
}
