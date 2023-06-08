package com.feidian.ChromosView;

import com.feidian.ChromosView.domain.Cultivar;
import com.feidian.ChromosView.mapper.ChromosomeMapper;
import com.feidian.ChromosView.mapper.CultivarMapper;
import com.feidian.ChromosView.mapper.SpeciesMapper;
import com.feidian.ChromosView.service.CompartmentService;
import com.feidian.ChromosView.utils.ReadFile;
import com.feidian.ChromosView.domain.Chromosome;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import javastraw.reader.Dataset;
//import javastraw.reader.basics.Chromosome;
import javastraw.reader.block.Block;
import javastraw.reader.block.ContactRecord;
import javastraw.reader.mzd.Matrix;
import javastraw.reader.mzd.MatrixZoomData;
import javastraw.reader.norm.NormalizationPicker;
import javastraw.reader.type.HiCZoom;
import javastraw.reader.type.NormalizationType;
import javastraw.tools.HiCFileTools;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@SpringBootTest
class ChromosViewTests {
    @Autowired
    CompartmentService compartmentService;
    @Autowired
    ChromosomeMapper chromosomeMapper;
    @Autowired
    SpeciesMapper speciesMapper;
    @Autowired
    CultivarMapper cultivarMapper;
    @Test
    void contextLoads() {
    }
    /*
    @Test
    void readTest(){
        // do you want to cache portions of the file?
        // this uses more RAM, but if you want to repeatedly
        // query nearby regions, it can improve speed by a lot
        boolean useCache = false;
        String filename = "D:/DNA/A2_matrix.hic";

        // create a hic dataset object
        Dataset ds = HiCFileTools.extractDatasetForCLT(filename, false, useCache, false);

        // pick the normalization we would like
        // this line will check multiple possible norms
        // and pick whichever is available (in order of preference)
        NormalizationType norm = NormalizationPicker.getFirstValidNormInThisOrder(ds, new String[]{"NONE","SCALE","KR","VC", "VC_SQRT" });
        System.out.println("Norm being used: " + norm.getLabel());

        // let's set our resolution
        int resolution = 5000;
        // let's grab the chromosomes
        int count=0,count2=0;
        Chromosome[] chromosomes = ds.getChromosomeHandler().getChromosomeArrayWithoutAllByAll();
        // now let's iterate on every chromosome (only intra-chromosomal regions for now)
        for (Chromosome chromosome : chromosomes) {
            count++;
            //count2=0;
            Matrix matrix = ds.getMatrix(chromosome, chromosome);
            if (matrix == null) continue;
            MatrixZoomData zd = matrix.getZoomData(new HiCZoom(resolution));
            if (zd == null) continue;
            // zd is now a data structure that contains pointers to the data
            // *** Let's show 2 different ways to access data ***
            // OPTION 1
            // iterate on all the data for the whole chromosome in sparse format
            Iterator<ContactRecord> iterator = zd.getNormalizedIterator(norm);

            while (iterator.hasNext()) {
                count2++;
                ContactRecord record = iterator.next();
                // now do whatever you want with the contact record
                int binX = record.getBinX();
                int binY = record.getBinY();
                float counts = record.getCounts();
                // binX and binY are in BIN coordinates, not genome coordinates
                // to switch, we can just multiply by the resolution
                int genomeX = binX * resolution;
                int genomeY = binY * resolution;
                if (counts > 0) { // will skip NaNs
                    // do task
//                    System.out.println(genomeX + " " + genomeY + " " + counts);

                    // the iterator only iterates above the diagonal
                    // to also fill in data below the diagonal, flip it
                    if (binX != binY) {
                        binX = record.getBinY();
                        binY = record.getBinX();
                        counts = record.getCounts();

                        genomeX = binX * resolution;
                        genomeY = binY * resolution;

//                        System.out.println(genomeX + " " + genomeY + " " + counts);
                        // do task
                    }
                }
            }
            //System.out.println(count);
            // OPTION 2
            // just grab sparse data for a specific region
            // choose your setting for when the diagonal is in the region
            boolean getDataUnderTheDiagonal = true;

            // our bounds will be binXStart, binYStart, binXEnd, binYEnd
            // these are in BIN coordinates, not genome coordinates
            int binXStart = 500, binYStart = 600, binXEnd = 1000, binYEnd = 1200;
            List<Block> blocks = zd.getNormalizedBlocksOverlapping(binXStart, binYStart, binXEnd, binYEnd, norm, getDataUnderTheDiagonal);
            for (Block b : blocks) {
                if (b != null) {
                    for (ContactRecord rec : b.getContactRecords()) {
                        if (rec.getCounts() > 0) { // will skip NaNs
                            // can choose to use the BIN coordinates
                            int binX = rec.getBinX();
                            int binY = rec.getBinY();
                            // you could choose to use relative coordinates for the box given
                            int relativeX = rec.getBinX() - binXStart;
                            int relativeY = rec.getBinY() - binYStart;
                            float counts = rec.getCounts();
                        }
                    }
                }
            }
            System.out.println(count+"条内的数量："+count2);
        }
        System.out.println("染色体数量"+count);
        // to iterate over the whole genome
        for (int i = 0; i < chromosomes.length; i++) {
            for (int j = i; i < chromosomes.length; i++) {
                Matrix matrix = ds.getMatrix(chromosomes[i], chromosomes[j]);
                if (matrix == null) continue;
                MatrixZoomData zd = matrix.getZoomData(new HiCZoom(resolution));
                if (zd == null) continue;
                if (i == j) {
                    // intra-chromosomal region
                } else {
                    // inter-chromosomal region
                }
            }
        }

    }*/
    @Test
    void insertTest(){
        compartmentService.addCS();
    }
    @Test
    void updateDATA(){
        File file=new File("D:/DNA/ID.xls");
        try {
            List list=ReadFile.readExcel(file);
            System.out.println(list);
            for (int i = 1; i < list.size(); i++) {
                List list1 = (List) list.get(i);
                System.out.println(list1);
                int speciesID=speciesMapper.findByName((String) list1.get(0));
                int cultivarID=cultivarMapper.findByName_SpeciesID(new Cultivar(0, new String((String) list1.get(1)).trim(),speciesID,0));
                System.out.println("品种id:"+cultivarID+" 染色体数量："+(int) Float.parseFloat((String) list1.get(2)));
                System.out.println(cultivarMapper.updateCSNum((int) Float.parseFloat((String) list1.get(2)),cultivarID));

            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @Test
    void addSpecies_Chromosome()
    {
        List<Cultivar> list1=cultivarMapper.findAll();
        List <Chromosome>list2=new ArrayList();
        for (Cultivar cultivar : list1) {
            List<Chromosome> list=chromosomeMapper.findMaxLength(cultivar.getCULTIVAR_ID(),cultivar.getCS_NUM());
            //System.out.println(list);
            list2.addAll(list);
        }
        //System.out.println(list2);
        chromosomeMapper.insertChromosome(list2);
    }
}



