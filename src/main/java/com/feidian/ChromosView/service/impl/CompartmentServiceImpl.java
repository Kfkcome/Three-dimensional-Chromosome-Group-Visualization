package com.feidian.ChromosView.service.impl;

import com.feidian.ChromosView.domain.Cultivar;
import com.feidian.ChromosView.domain.Species;
import com.feidian.ChromosView.mapper.ChromosomeMapper;
import com.feidian.ChromosView.mapper.CultivarMapper;
import com.feidian.ChromosView.mapper.SpeciesMapper;
import com.feidian.ChromosView.service.CompartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;

@Service
public class CompartmentServiceImpl implements CompartmentService {
    private final SpeciesMapper speciesMapper;
    private final CultivarMapper cultivarMapper;
    private final ChromosomeMapper chromosomeMapper;
    @Autowired
    public CompartmentServiceImpl(SpeciesMapper speciesMapper, CultivarMapper cultivarMapper, ChromosomeMapper chromosomeMapper) {
        this.speciesMapper = speciesMapper;
        this.cultivarMapper = cultivarMapper;
        this.chromosomeMapper = chromosomeMapper;
    }

    public int addSpecies_Chromosome_raw() {
        File file = new File("C:/Users/15858/Documents/WeChat Files/wxid_7x1dm3fi3js422/FileStorage/File/2023-06/Chr_ID");
        File[] files = file.listFiles();
        for (File file1 : files) {
            String name = file1.getName();
            //System.out.println(name);
            String[] split = name.split(".genome")[0].split("_");
            System.out.println(split[0]+" "+split[1]);
            //System.out.println(speciesMapper.findByName(split[0]));
            Species species=new Species();
            species.setSPECIES_NAME(split[0]);
            if(speciesMapper.findByName(split[0])==null){
                speciesMapper.addSpecies(species);
            }
            Cultivar cultivar=new Cultivar();
            cultivar.setCULTIVAR_NAME(split[1]);
            cultivar.setSPECIES_ID(speciesMapper.findByName(split[0]));
            cultivarMapper.addCultivar(cultivar);
        }
        return 0;
    }
    public int addSpecies_Chromosome()
    {
        return 0;
    }
    public int addCS(){
        File file = new File("C:/Users/15858/Documents/WeChat Files/wxid_7x1dm3fi3js422/FileStorage/File/2023-06/Chr_ID");
        File[] files = file.listFiles();
        for (File file1 : files) {
            try {
                String data;
                String name = file1.getName();
                BufferedReader reader = new BufferedReader(new FileReader(file1));
                System.out.println("--------------------"+ name);
                while((data=reader.readLine())!=null){
                    String[] split = name.split(".genome")[0].split("_");
                    Cultivar cultivar=new Cultivar();
                    cultivar.setCULTIVAR_NAME(split[1]);
                    int spe_id=speciesMapper.findByName(split[0]);
                    cultivar.setSPECIES_ID(spe_id);
                    int id=cultivarMapper.findByName_SpeciesID(cultivar);
                    String []dataN=data.split("\t");
                    long length=new Long(dataN[1]);
                    //System.out.println(length);
                    //chromosomeMapper.insertChromosome(new Chromosome(0,dataN[0],length,id));
                }

            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return 0;
    }

//    public static void main(String[] args) {
//        File file=new File("C:/Users/15858/Documents/WeChat Files/wxid_7x1dm3fi3js422/FileStorage/File/2023-06/Chr_ID");
//        File[] files=file.listFiles();
//        for (File file1 : files) {
//            String name = file1.getName();
//            //System.out.println(name);
//            String[] split = name.split(".genome");
//            System.out.println(split[0]);
//        }
//
//
//    }
}
