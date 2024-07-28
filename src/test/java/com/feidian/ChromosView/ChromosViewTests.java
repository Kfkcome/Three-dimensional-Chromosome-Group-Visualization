package com.feidian.ChromosView;

import com.feidian.ChromosView.controller.ExcelController;
import com.feidian.ChromosView.service.ExcelService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ChromosViewTests {
    @Autowired
    ExcelService excelService;
    @Autowired
    ExcelController excelController;

    @Test
    public void testExcel() {
        for (Object excelTad : excelService.getExcelData("excel_tad", 10, 10)) {
            System.out.println(excelTad.toString());
        }
    }
}



