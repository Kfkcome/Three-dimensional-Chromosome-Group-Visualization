package com.feidian.ChromosView.mapper;

import com.feidian.ChromosView.domain.Chromosome;
import com.feidian.ChromosView.domain.CompartmentPoint;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface CompartmentMapper {
    int insertFromFile(List<CompartmentPoint> compartmentPoints);
    List<CompartmentPoint> findCompartmentID(@Param("cs_id") int cs_id);
}
