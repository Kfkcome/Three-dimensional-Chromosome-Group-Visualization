package com.feidian.ChromosView.mapper;

import com.feidian.ChromosView.domain.CompartmentPoint;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface CompartmentMapper {
    int insertFromFile(List<CompartmentPoint> compartmentPoints);
}
