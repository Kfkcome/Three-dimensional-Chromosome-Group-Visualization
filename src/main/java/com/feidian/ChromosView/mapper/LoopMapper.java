package com.feidian.ChromosView.mapper;

import com.feidian.ChromosView.domain.LoopPoint;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface LoopMapper {
    int insertFromFile(List<LoopPoint> item);

    List<LoopPoint> findAllPoint(@Param("cs_id") int cs_id);

    List<LoopPoint> findPointBySTART_EDN(@Param("cs_id") int cs_id, @Param("start") long start, @Param("end") long end);

    List<LoopPoint> findPointByDoublePoint(@Param("cs_id") int cs_id, @Param("start1") long start1,
                                           @Param("end1") long end1, @Param("start2") long start2, @Param("end2") long end2);
}
