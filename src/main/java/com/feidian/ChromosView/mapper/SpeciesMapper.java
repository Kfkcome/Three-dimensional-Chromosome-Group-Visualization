package com.feidian.ChromosView.mapper;

import com.feidian.ChromosView.domain.Species;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface SpeciesMapper {
    List<Species> findAll();

    Integer findByName(String name);

    int addSpecies(Species species);
}
