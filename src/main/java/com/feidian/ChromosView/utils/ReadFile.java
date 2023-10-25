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
                for (int colNum = 0; colNum < Row.getLastCellNum(); colNum++) {
                    rowList.add(colNum, Row.getCell(colNum).toString());
                }
                aList.add(rowList);
            }
        }
        return aList;
    }

    public static int convertResolution(String resolutionT) {
        int resolution;
        if (resolutionT != null && resolutionT != "") {
            resolution = Integer.parseInt(resolutionT);
        } else resolution = 5000;
        return resolution;
    }

    public static ArrayList<MatrixPoint> readHICALL(File file, String CSName1, String CSName2, String norms, String resolutionT) {

        // do you want to cache portions of the file?
        // this uses more RAM, but if you want to repeatedly
        // query nearby regions, it can improve speed by a lot
        boolean useCache = true;
        String filename = file.getPath();

        // create a hic dataset object
        Dataset ds = HiCFileTools.extractDatasetForCLT(filename, false, useCache, false);
        // create a class to save the result
        //ArrayList<ArrayList<MatrixPoint>> matrixPoints=new ArrayList<>();

        // pick the normalization we would like
        // this line will check multiple possible norms: {"KR", "SCALE", "VC", "VC_SQRT", "NONE" }
        // and pick whichever is available (in order of preference)
        NormalizationType norm = NormalizationPicker.getFirstValidNormInThisOrder(ds, new String[]{norms});
        System.out.println("Norm being used: " + norm.getLabel());

        // let's set our resolution点之间的间隔
        int resolution = convertResolution(resolutionT);
        // let's grab the chromosomes
        int count = 0, count2 = 0;
        Chromosome[] chromosome = ds.getChromosomeHandler().getChromosomeArrayWithoutAllByAll();


        // now let's iterate on every chromosome (only intra-chromosomal regions for now)
        ArrayList<MatrixPoint> matrixPoints1 = new ArrayList<MatrixPoint>();
        endloop:
        for (int i = 0; i < chromosome.length; i++) {
            for (int j = 0; j < chromosome.length; j++) {
                System.out.println("查询到的名称：" + chromosome[i].getName() + "  " + chromosome[j].getName());
                //如果想要的染色与此条不是一条则跳过
                if (!(chromosome[i].getName().equals(CSName1) && chromosome[j].getName().equals(CSName2)))
                    continue;
                System.out.println("  查询到染色体！");
                System.out.println("长度" + chromosome[i].getLength() + "  " + chromosome[j].getLength());
                Matrix matrix = ds.getMatrix(chromosome[i], chromosome[j]);
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
                    long binX = record.getBinX();
                    long binY = record.getBinY();
                    float counts = record.getCounts();
                    // binX and binY are in BIN coordinates, not genome coordinates
                    // to switch, we can just multiply by the resolution
                    long genomeX;
                    long genomeY;
                    // will skip NaNs
                    // do task
                    //System.out.println(genomeX + " " + genomeY + " " + counts);
                    // the iterator only iterates above the diagonal
                    // to also fill in data below the diagonal, flip it
                    if (binY >= 0 && binX >= 0) {
                        binX = record.getBinX() * resolution;
                        binY = record.getBinY() * resolution;
                        counts = record.getCounts();
                        genomeX = binX * resolution;
                        genomeY = binY * resolution;
                        count++;
                        matrixPoints1.add(new MatrixPoint(binX / 1000000.0, binY / 1000000.0, genomeX, genomeY, counts));
                        //System.out.println(genomeX + " " + genomeY + " " + counts);
                        // do task
                    }

                }
                if (count > 0) {
                    System.out.println("查询完毕 查询到：" + count + "条数据");
                    break endloop;
                }
            }
        }

        // to iterate over the whole genome
        return matrixPoints1;
    }

    public static ArrayList<MatrixPoint> readHICByStart_End(File file, String
            CSName1, String CSName2, String norms, Long binXStart, Long binYStart, Long binXEnd, Long binYEnd, String resolutionT) {
        // do you want to cache portions of the file?
        // this uses more RAM, but if you want to repeatedly
        // query nearby regions, it can improve speed by a lot
        boolean useCache = false;
        String filename = file.getPath();

        // create a hic dataset object
        Dataset ds = HiCFileTools.extractDatasetForCLT(filename, false, useCache, false);

        // pick the normalization we would like
        // this line will check multiple possible norms
        // and pick whichever is available (in order of preference)
        NormalizationType norm = NormalizationPicker.getFirstValidNormInThisOrder(ds, new String[]{norms});
        System.out.println("Norm being used: " + norm.getLabel());

        //creat a set to save the result
        ArrayList<MatrixPoint> matrixPointArrayList = new ArrayList<>();

        // let's set our resolution\
        int resolution;
        if (resolutionT == null || resolutionT == "")
            resolution = 5000;
        else resolution = Integer.parseInt(resolutionT);

        // let's grab the chromosomes
        Chromosome[] chromosome = ds.getChromosomeHandler().getChromosomeArrayWithoutAllByAll();
        int count = 0;
        // now let's iterate on every chromosome (only intra-chromosomal regions for now)
        endLoop:
        for (int i = 0; i < chromosome.length; i++) {
            for (int j = 0; j < chromosome.length; j++) {
                System.out.println("查询到的名称：" + chromosome[i].getName() + "  " + chromosome[j].getName());
                if (!(chromosome[i].getName().equals(CSName1) && chromosome[j].getName().equals(CSName2)))
                    continue;
                System.out.println("  查询到染色体！");

                System.out.println("长度：" + chromosome[i].getLength() + "  " + chromosome[j].getLength());
                Matrix matrix = ds.getMatrix(chromosome[i], chromosome[j]);
                if (matrix == null) continue;
                MatrixZoomData zd = matrix.getZoomData(new HiCZoom(resolution));
                if (zd == null) continue;

                // zd is now a data structure that contains pointers to the data
                // *** Let's show 2 different ways to access data ***
                // OPTION 2
                // just grab sparse data for a specific region

                // choose your setting for when the diagonal is in the region
                boolean getDataUnderTheDiagonal = true;

                // our bounds will be binXStart, binYStart, binXEnd, binYEnd
                // these are in BIN coordinates, not genome coordinates

                List<Block> blocks = zd.getNormalizedBlocksOverlapping(binXStart, binYStart, binXEnd, binYEnd, norm, getDataUnderTheDiagonal);
                for (Block b : blocks) {
                    if (b != null) {
                        for (ContactRecord rec : b.getContactRecords()) {
                            int binX = rec.getBinX();
                            int binY = rec.getBinY();
                            if (checkInSpace(binX, binY, binXStart, binXEnd, binYStart, binYEnd)) { // will skip NaNs
                                // can choose to use the BIN coordinates
                                count++;
                                // you could choose to use relative coordinates for the box given
                                long genomeX = rec.getBinX() * resolution - binXStart * resolution;
                                long genomeY = rec.getBinY() * resolution - binYStart * resolution;
                                binX = rec.getBinX() * resolution;
                                binY = rec.getBinY() * resolution;
                                float counts = rec.getCounts();
                                matrixPointArrayList.add(new MatrixPoint(binX / 1000000.0, binY / 1000000.0, genomeX, genomeY, counts));
                            }
                        }
                    }
                }
                if (count > 0) {
                    System.out.println("查询完毕 查询到：" + count + "条数据");
                    break endLoop;
                }
            }
        }
        return matrixPointArrayList;
    }

    private static boolean checkInSpace(int binX, int binY, Long binXStart, Long binXEnd, Long binYStart, Long binYEnd) {
        return binX <= binXEnd && binX >= binXStart && binY <= binYEnd && binY >= binYStart;
    }

}
