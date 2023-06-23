package com.feidian.ChromosView.mapper;

import com.feidian.ChromosView.domain.RNA;
import com.feidian.ChromosView.domain.RNA_STRUCTURE;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface RnaMapper {
    Integer insertRnaFromFile(List<RNA> rna);

    Integer insertRna_StructureFromFile(List<RNA_STRUCTURE> rnaStructures);

    Integer getMaxID();

    RNA selectRnaByID(@Param("mRNA_NAME") String mRNA_NAME, @Param("CS_ID") int CS_ID);

    List<RNA_STRUCTURE> selectRnaStructByID(@Param("mRNA_ID") int mRnaId);

    List<RNA> selectRnaByCS_ID_START_END(@Param("CS_ID") int cs_id, @Param("START_POINT") long startPoint
            , @Param("END_POINT") long endPoint);

    List<RNA> selectRnaByCS_ID(@Param("CS_ID") int cs_id);
}
