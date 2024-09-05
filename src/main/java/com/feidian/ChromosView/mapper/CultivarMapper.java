package com.feidian.ChromosView.mapper;

import com.feidian.ChromosView.domain.Cultivar;
import com.feidian.ChromosView.domain.Software;
import com.feidian.ChromosView.domain.SoftwareOfAnnotation2D;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface CultivarMapper {
    int addCultivar(Cultivar cultivar);

    List<String> findAll();

    List<String> findBCLABySpeciesID(@Param("SPECIES_NAME") String SPECIES_NAME);

    int findByName_SpeciesID(Cultivar cultivar);

    Integer updateCSNum(@Param("CS_NUM") int CS_NUM, @Param("CULTIVAR_ID") int CULTIVAR_ID);

    List<String> findTissueByCultivar(@Param("SpeciesName") String SpeciesName, @Param("CultivarName") String CultivarName);

    Integer findMinIdByCultivar(@Param("SpeciesName") String SpeciesName, @Param("CultivarName") String CultivarName);


    List<Software> findAllSoftware();

    Integer findByName(@Param("SpeciesName") String SpeciesName, @Param("CultivarName") String CultivarName, @Param("TissueName") String TissueName);

    SoftwareOfAnnotation2D findTheSoftware(@Param("CultivarID") int CultivarID);

//    List<String> findTissueByCultivarName()

}
