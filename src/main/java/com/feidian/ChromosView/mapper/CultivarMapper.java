package com.feidian.ChromosView.mapper;

import com.feidian.ChromosView.domain.Cultivar;
import com.feidian.ChromosView.domain.Software;
import com.feidian.ChromosView.domain.Tissue;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface CultivarMapper {
    int addCultivar(Cultivar cultivar);

    List<String> findAll();

    Cultivar findOneByTissueId(@Param("cultivar_id") int cultivar_id);

    List<String> findBCLABySpeciesID(@Param("SPECIES_NAME") String SPECIES_NAME);

    int findByName_SpeciesID(Cultivar cultivar);

    Integer updateCSNum(@Param("CS_NUM") int CS_NUM, @Param("CULTIVAR_ID") int CULTIVAR_ID);

    List<String> findTissueByCultivar(@Param("SpeciesName") String SpeciesName, @Param("CultivarName") String CultivarName);

    Integer findMinIdByCultivar(@Param("SpeciesName") String SpeciesName, @Param("CultivarName") String CultivarName);

    Tissue findTissueByID(int TISSUE_ID);

    List<Software> findAllSoftware();

//    List<String> findTissueByCultivarName()

}
