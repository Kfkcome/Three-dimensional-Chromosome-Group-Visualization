package com.feidian.ChromosView.mapper;

import com.feidian.ChromosView.domain.Chromosome;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface ChromosomeMapper {
    int insertChromosome(List<Chromosome> chromosome);
    List<Chromosome> findMaxLength(@Param("CULTIVATE_ID") int CULTIVATE_ID,@Param("limit") int limit);
    List<Chromosome> findAll();
    List<Chromosome> findByCultivarID(@Param("id")int id);
    Chromosome findByCultivarID_CSName(@Param("id")int id,@Param("name") String name);

}
