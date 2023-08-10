package com.feidian.ChromosView;

import com.feidian.ChromosView.domain.ChromosomeT;
import com.feidian.ChromosView.domain.Cultivar;
import com.feidian.ChromosView.mapper.ChromosomeMapper;
import com.feidian.ChromosView.mapper.CultivarMapper;
import com.feidian.ChromosView.mapper.SpeciesMapper;
import com.feidian.ChromosView.service.CompartmentService;
import com.feidian.ChromosView.service.impl.LoopServiceImpl;
import com.feidian.ChromosView.service.impl.RnaServiceImpl;
import com.feidian.ChromosView.utils.ReadFile;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class ChromosViewTests {
    @Autowired
    DataSource dataSource;
    @Autowired
    CompartmentService compartmentService;
    @Autowired
    ChromosomeMapper chromosomeMapper;
    @Autowired
    SpeciesMapper speciesMapper;
    @Autowired
    CultivarMapper cultivarMapper;
    @Autowired
    LoopServiceImpl loopService;
    @Autowired
    RnaServiceImpl rnaService;

    @Test
    void contextLoads() {
        //查看默认的数据源
        System.out.println(dataSource.getClass());

        //获得数据库连接
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
        } catch (SQLException e) {


        }
        System.out.println(connection);
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void insertTest() {
        compartmentService.addCS();
    }

    @Test
    void updateDATA() {
        File file = new File("D:/DNA/ID.xls");
        try {
            List list = ReadFile.readExcel(file);
            System.out.println(list);
            for (int i = 1; i < list.size(); i++) {
                List list1 = (List) list.get(i);
                System.out.println(list1);
                int speciesID = speciesMapper.findByName((String) list1.get(0));
                int cultivarID = cultivarMapper.findByName_SpeciesID(new Cultivar(0, new String((String) list1.get(1)).trim(), speciesID, 0));
                System.out.println("品种id:" + cultivarID + " 染色体数量：" + (int) Float.parseFloat((String) list1.get(2)));
                System.out.println(cultivarMapper.updateCSNum((int) Float.parseFloat((String) list1.get(2)), cultivarID));

            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void addSpecies_Chromosome() {
        List<Cultivar> list1 = cultivarMapper.findAll();
        List<ChromosomeT> list2 = new ArrayList<>();
        for (Cultivar cultivar : list1) {
            List<ChromosomeT> list = chromosomeMapper.findMaxLength(cultivar.getCULTIVAR_ID(), cultivar.getCS_NUM());
            //System.out.println(list);
            list2.addAll(list);
        }
        //System.out.println(list2);
        chromosomeMapper.insertChromosome(list2);
    }

    @Test
    void insertFromFile() {
        compartmentService.addPointFromFile("D:\\DNA\\A2_Compartment.bed");
    }

    @Test
    void insertLoopFromFile() {
        loopService.insertDataFromFile("D:\\DNA\\A2_loop.txt");
    }

    @Test
    void insertRNAFromFile() {
        System.out.println(rnaService.insertRnaFromFile("D:\\DNA\\A2_gene.gff3"));
    }
}



