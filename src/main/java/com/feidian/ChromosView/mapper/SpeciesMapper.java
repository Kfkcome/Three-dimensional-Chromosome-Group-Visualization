package com.feidian.ChromosView.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface SpeciesMapper {
    List<String> findAll();

}
