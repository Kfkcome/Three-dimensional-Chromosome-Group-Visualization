package com.feidian.ChromosView.mapper;

import com.feidian.ChromosView.domain.CompartmentPoint;
import com.feidian.ChromosView.domain.CompartmentPointMB;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface CompartmentMapper {
    int insertFromFile(List<CompartmentPointMB> compartmentPoints);
    List<CompartmentPoint> findCompartmentID(@Param("cs_id") int cs_id,@Param("tissue_id")int tissue_id,@Param("software_id")int software_id);
    List<CompartmentPoint> findCompartmentEND_START(@Param("cs_id")int cs_id
            ,@Param("start")int start,@Param("end")int end,@Param("tissue_id")int tissue_id,@Param("software_id")int software_id);
}
