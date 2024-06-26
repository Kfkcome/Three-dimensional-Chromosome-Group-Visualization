package com.feidian.ChromosView.service.impl;

import GenerateMyHeatMap.GenerateHeatmap;
import com.feidian.ChromosView.domain.*;
import com.feidian.ChromosView.mapper.ChromosomeMapper;
import com.feidian.ChromosView.mapper.CompartmentMapper;
import com.feidian.ChromosView.mapper.CultivarMapper;
import com.feidian.ChromosView.mapper.SpeciesMapper;
import com.feidian.ChromosView.service.CompartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class CompartmentServiceImpl implements CompartmentService {
    private final SpeciesMapper speciesMapper;
    private final CultivarMapper cultivarMapper;
    private final ChromosomeMapper chromosomeMapper;
    private final CompartmentMapper compartmentMapper;
    private final GenerateHeatmap generateHeatmap;

    @Autowired
    public CompartmentServiceImpl(SpeciesMapper speciesMapper, CultivarMapper cultivarMapper, ChromosomeMapper chromosomeMapper, CompartmentMapper compartmentMapper) {
        this.speciesMapper = speciesMapper;
        this.cultivarMapper = cultivarMapper;
        this.chromosomeMapper = chromosomeMapper;
        this.compartmentMapper = compartmentMapper;
        generateHeatmap = GenerateHeatmap.getInstance();
    }

    public int addSpecies_Chromosome_raw() {
        File file = new File("C:/Users/15858/Documents/WeChat Files/wxid_7x1dm3fi3js422/FileStorage/File/2023-06/Chr_ID");
        File[] files = file.listFiles();
        for (File file1 : files) {
            String name = file1.getName();
            //System.out.println(name);
            String[] split = name.split(".genome")[0].split("_");
            System.out.println(split[0] + " " + split[1]);
            //System.out.println(speciesMapper.findByName(split[0]));
            Species species = new Species();
            species.setSPECIES_NAME(split[0]);
            if (speciesMapper.findByName(split[0]) == null) {
                speciesMapper.addSpecies(species);
            }
            Cultivar cultivar = new Cultivar();
            cultivar.setCULTIVAR_NAME(split[1]);
            cultivar.setSPECIES_ID(speciesMapper.findByName(split[0]));
            cultivarMapper.addCultivar(cultivar);
        }
        return 0;
    }

    public int addSpecies_Chromosome() {
        return 0;
    }

    public int addCS() {
        File file = new File("C:/Users/15858/Documents/WeChat Files/wxid_7x1dm3fi3js422/FileStorage/File/2023-06/Chr_ID");
        File[] files = file.listFiles();
        for (File file1 : files) {
            try {
                String data;
                String name = file1.getName();
                BufferedReader reader = new BufferedReader(new FileReader(file1));
                System.out.println("--------------------" + name);
                while ((data = reader.readLine()) != null) {
                    String[] split = name.split(".genome")[0].split("_");
                    Cultivar cultivar = new Cultivar();
                    cultivar.setCULTIVAR_NAME(split[1]);
                    int spe_id = speciesMapper.findByName(split[0]);
                    cultivar.setSPECIES_ID(spe_id);
                    int id = cultivarMapper.findByName_SpeciesID(cultivar);
                    String[] dataN = data.split("\t");
                    long length = new Long(dataN[1]);
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

    @Override
    public int addPointFromFile(String path) {
        File file = new File(path);
        ArrayList<CompartmentPointMB> compartmentPointArrayList = new ArrayList<>();
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            String data;
            while ((data = bufferedReader.readLine()) != null) {
                String[] split = data.split("\t");
                ChromosomeT byCultivarIDCsName = chromosomeMapper.findByCultivarID_CSName(67, split[0].trim());
                compartmentPointArrayList.add(new CompartmentPointMB(1
                        , byCultivarIDCsName.getCS_ID()
                        , Long.parseLong(split[1]), Long.parseLong(split[2])
                        , Double.parseDouble(split[3])));
            }
            compartmentMapper.insertFromFile(compartmentPointArrayList);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }

    @Override
    public List<CompartmentPoint> findPointByCS_ID(int id, int tissue_id, int software_id) {
        List<CompartmentPoint> compartment = compartmentMapper.findCompartmentID(id, tissue_id, software_id);
        return compartment;
    }

    @Override
    public List<CompartmentPoint> findPointByEND_START(int cs_id, String startT, String endT, int tissue_id, int software_id) {
        Integer end;
        Integer start;
        List<CompartmentPoint> compartmentENDStart;
        if (startT != null && endT != null && endT != "" && startT != "") {
            end = Integer.parseInt(endT);
            start = Integer.parseInt(startT);
            compartmentENDStart = compartmentMapper.findCompartmentEND_START(cs_id, start, end, tissue_id, software_id);
        } else {
            compartmentENDStart = compartmentMapper.findCompartmentID(cs_id, tissue_id, software_id);
        }
        return compartmentENDStart;
    }


    private String uniteFileNameToHICPath(String species, String cultivar, String tissue) {
        return species + "_" + cultivar + "_" + tissue;
    }

    private String uniteFileNameToAnnotationPath(String species, String cultivar) {
        return species + "_" + cultivar;
    }

    @Override
    public Boolean generateCompartment(String species, String cultivar, String tissue, String chromosome, Integer clarity, HttpServletResponse response) {
        BufferedImage temp;
        String fileName = uniteFileNameToHICPath(species, cultivar, tissue);
        String path = "hic/" + fileName + ".hic";
        String annotationName = uniteFileNameToHICPath(species, cultivar, tissue);
        String annotationPath = "Compartment/" + annotationName + ".bw";
        if (clarity == null) {
            clarity = 1;//默认分辨率为1
        }
        try {
            temp = generateHeatmap.generateAnnotation1D(path, annotationPath, chromosome, clarity);
        } catch (IOException e) {
            throw new RuntimeException(e);
            //TODO：处理生成Compartment图生成失败
        }
        if (temp == null) {
            return false;
        }
        try {
            OutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
            ImageIO.write(temp, "png", outputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
            //todo:实现生成图片的异常处理
        }
        return true;
    }

    @Override
    public String getPointData(String species, String cultivar, String tissue, String chromosome, int x, int y) {
        String data = null;
        try {
            data = generateHeatmap.getAnnotation1DData("./Gossypium-hirsutum_TM-1_Leaf.hic", "./Compartment.bw", chromosome, x, y);
        } catch (IOException e) {
            //TODO：处理找不到点的异常
        }
        return data;
    }

}
