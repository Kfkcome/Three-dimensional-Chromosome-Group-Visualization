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
    List<CompartmentPoint> findCompartmentID(@Param("cs_id") int cs_id);
    List<CompartmentPoint> findCompartmentEND_START(@Param("cs_id")int cs_id
            ,@Param("start")int start,@Param("end")int end);
}
