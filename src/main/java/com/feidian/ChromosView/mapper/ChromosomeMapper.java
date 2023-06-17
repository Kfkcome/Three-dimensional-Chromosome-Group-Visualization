package com.feidian.ChromosView.mapper;

import com.feidian.ChromosView.domain.ChromosomeT;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface ChromosomeMapper {
    int insertChromosome(List<ChromosomeT> chromosomeT);
    List<ChromosomeT> findMaxLength(@Param("CULTIVATE_ID") int CULTIVATE_ID, @Param("limit") int limit);
    List<ChromosomeT> findAll();
    List<ChromosomeT> findByCultivarID(@Param("id")int id);
    ChromosomeT findByCultivarID_CSName(@Param("id")int id, @Param("name") String name);

}
