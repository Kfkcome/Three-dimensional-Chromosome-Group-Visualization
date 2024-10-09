package com.feidian.ChromosView.mapper;

import com.feidian.ChromosView.domain.ConBtwSpe;
import com.feidian.ChromosView.domain.CpData;
import com.feidian.ChromosView.domain.LoopData;
import com.feidian.ChromosView.domain.TadData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface CpBtwCulMapper {
    List<String> getGenusName();

    List<String> getSpeciesName(@Param("genus") String genus);

    List<String> getCultivarName(@Param("genus") String genus, @Param("species") String species);

    List<String> getChromosomeName(@Param("target") String target);

    List<ConBtwSpe> getConnectionALL(@Param("cs1_name") String cs_name1, @Param("table_name") String table_name);

    Integer getConnectionKind(@Param("cs1_name") String cs_name1, @Param("table_name") String table_name);

    Integer getConnectionKindByRange(@Param("cs1_name") String cs_name1, @Param("table_name") String table_name
            , @Param("cs1_start") int s1, @Param("cs1_end") int e1);

    List<ConBtwSpe> getConnectionByRange(@Param("cs1_name") String cs_name1, @Param("table_name") String table_name
            , @Param("cs1_start") int s1, @Param("cs1_end") int e1);


    List<TadData> getTadDataByChromosome(@Param("table_name") String table_name, @Param("cs_name") String chromosome);

    List<TadData> getTadDataByRange(@Param("table_name") String table_name, @Param("cs_name") String chromosome, @Param("start") int start, @Param("end") int end);

    List<LoopData> getLoopDataByChromosome(@Param("table_name") String table_name, @Param("cs_name") String chromosome);

    List<LoopData> getLoopDataByRange(@Param("table_name") String table_name, @Param("cs_name") String chromosome, @Param("start") int start, @Param("end") int end, @Param("threshold") double threshold);

    List<CpData> getCompartmentDataByChromosome(@Param("table_name") String table_name, @Param("cs_name") String chromosome);

    List<CpData> getCompartmentDataByRange(@Param("table_name") String table_name, @Param("cs_name") String chromosome, @Param("start") int start, @Param("end") int end);
}
