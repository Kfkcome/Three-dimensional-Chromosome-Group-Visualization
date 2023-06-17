package com.feidian.ChromosView.utils;

import com.feidian.ChromosView.domain.MatrixPoint;
import javastraw.reader.Dataset;
import javastraw.reader.basics.Chromosome;
import javastraw.reader.block.Block;
import javastraw.reader.block.ContactRecord;
import javastraw.reader.mzd.Matrix;
import javastraw.reader.mzd.MatrixZoomData;
import javastraw.reader.norm.NormalizationPicker;
import javastraw.reader.type.HiCZoom;
import javastraw.reader.type.NormalizationType;
import javastraw.tools.HiCFileTools;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class ReadFile {
    public static List readExcel(File file) throws Exception {      //读取excel函数
        //获取文件名字
        String fileName = file.getName();
        //获取文件类型
        String fileType = fileName.substring(fileName.lastIndexOf(".") + 1);
        System.out.println(" **** fileType:" + fileType);
        //获取输入流
        InputStream stream = new FileInputStream(file);
        //获取工作薄
        Workbook xssfWorkbook = null;
        if (fileType.equals("xls")) {
            xssfWorkbook = new HSSFWorkbook(stream);
        } else if (fileType.equals("xlsx")) {
            xssfWorkbook = new XSSFWorkbook(stream);
        } else {
            System.out.println("您输入的excel格式不正确");
        }
        //     TranTaskDao a = null;
        List aList = new ArrayList();
        // Read the Sheet
        Sheet Sheet = xssfWorkbook.getSheetAt(0);
        // Read the Row 从0开始
        for (int rowNum = 0; rowNum <= Sheet.getLastRowNum(); rowNum++) {
            Row Row = Sheet.getRow(rowNum);
            if (Row != null) {
                //判断这行记录是否存在
                if (Row.getLastCellNum() < 1 || "".equals(Row.getCell(0))) {
                    continue;
                }
                //获取每一行封装成对象
                List<String> rowList = new ArrayList<String>();
                for(int colNum=0;colNum<Row.getLastCellNum();colNum++)
                {
                    rowList.add(colNum, Row.getCell(colNum).toString());
                }
                aList.add(rowList);
            }
        }
        return aList;
    }
    public static ArrayList<ArrayList<MatrixPoint>> readHICALL(File file){
        // do you want to cache portions of the file?
        // this uses more RAM, but if you want to repeatedly
        // query nearby regions, it can improve speed by a lot
        boolean useCache = true;
        String filename = file.getPath();

        // create a hic dataset object
        Dataset ds = HiCFileTools.extractDatasetForCLT(filename, false, useCache, false);
        // create a class to save the result
        ArrayList<ArrayList<MatrixPoint>> matrixPoints=new ArrayList<>();
        // pick the normalization we would like
        // this line will check multiple possible norms
        // and pick whichever is available (in order of preference)
        NormalizationType norm = NormalizationPicker.getFirstValidNormInThisOrder(ds, new String[]{"NONE","SCALE","KR","VC", "VC_SQRT" });
        System.out.println("Norm being used: " + norm.getLabel());

        // let's set our resolution点之间的间隔
        int resolution = 5000;
        // let's grab the chromosomes
        int count=0,count2=0;
        Chromosome[] chromosome = ds.getChromosomeHandler().getChromosomeArrayWithoutAllByAll();
        // now let's iterate on every chromosome (only intra-chromosomal regions for now)
        for (Chromosome chromosomeT : chromosome) {
            System.out.println(chromosomeT.getName());
            System.out.println(chromosomeT.getLength());
            ArrayList<MatrixPoint> matrixPoints1=new ArrayList<>();
            count++;
            //count2=0;
            Matrix matrix = ds.getMatrix(chromosomeT, chromosomeT);
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
                    //System.out.println(genomeX + " " + genomeY + " " + counts);
                    // the iterator only iterates above the diagonal
                    // to also fill in data below the diagonal, flip it
                    if (binX != binY) {
                        binX = record.getBinY();
                        binY = record.getBinX();
                        counts = record.getCounts();

                        genomeX = binX * resolution;
                        genomeY = binY * resolution;
                        matrixPoints1.add(new MatrixPoint(binX,binY,genomeX,genomeY,counts));
//                        System.out.println(genomeX + " " + genomeY + " " + counts);
                        // do task
                    }
                }
            }
        matrixPoints.add(matrixPoints1);
            System.out.println(1);
        }
            System.out.println(count+"条内的数量："+count2);
        System.out.println("染色体数量"+count);
        // to iterate over the whole genome
        return matrixPoints;
    }
}
