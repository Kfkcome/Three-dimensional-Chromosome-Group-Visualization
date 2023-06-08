package com.feidian.ChromosView.utils;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
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
}
