package com.feidian.ChromosView.service.impl;

import com.feidian.ChromosView.exception.QueryException;
import com.feidian.ChromosView.mapper.ExcelMapper;
import com.feidian.ChromosView.service.ExcelService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@Service
public class ExcelServiceImpl<T> implements ExcelService<T> {
    final ExcelMapper excelMapper;
    HashMap<String, String> domain_to_table;

    public ExcelServiceImpl(ExcelMapper excelMapper) {
        this.excelMapper = excelMapper;
        domain_to_table = new HashMap<>();
        domain_to_table.put("excel_comparation", "ExcelComparation");
        domain_to_table.put("excel_compartment", "ExcelCompartment");
        domain_to_table.put("excel_genomics", "ExcelGenomics");
        domain_to_table.put("excel_hic_matrix", "ExcelHiCMatrix");
        domain_to_table.put("excel_loop", "ExcelLoop");
        domain_to_table.put("excel_tad", "ExcelTad");
        domain_to_table.put("excel_data_overview", "ExcelDataOverview");
    }


    private ArrayList<String> getExcelOrder(String tableName) {
        ArrayList<String> orderList;
        if (tableName.equals("excel_hic_matrix"))
            orderList = new ArrayList<>(Arrays.asList("Specie", "Cultivar", "Tissue", "Matrix (10K HiC-Pro)", "Bed (10K HiC-Pro)", "Matrix (40K HiC-Pro)", "Bed (40K HiC-Pro)", "Matrix (100K HiC-Pro)", "Bed (100K HiC-Pro)", "allValidPairs Files", "atrix (.hic format)"));
        else if (tableName.equals("excel_comparation"))
            orderList = new ArrayList<>(Arrays.asList("Genus", "Tcbf", "JCVI"));
        else if (tableName.equals("excel_compartment"))
            orderList = new ArrayList<>(Arrays.asList("Specie", "Cultivar", "Tissue", "Cworld (bed format)"));
        else if (tableName.equals("excel_genomics"))
            orderList = new ArrayList<>(Arrays.asList("Specie",
                    "Cultivar",
                    "Chromosomes + Scaffolds (FASTA Files)",
                    "Predicted Genes (GFF3 Files)",
                    "Predicted coding sequences (FASTA Files)",
                    "Predicted protein sequences (FASTA Files)",
                    "REF"));
        else if (tableName.equals("excel_loop"))
            orderList = new ArrayList<>(Arrays.asList("Specie",
                    "Cultivar",
                    "Tissue",
                    "Fit-HiC",
                    "HiCCUPS",
                    "HiCExplorer"));
        else if (tableName.equals("excel_tad"))
            orderList = new ArrayList<>(Arrays.asList("Specie",
                    "Cultivar", "Tissue", "Arrowhead", "ClusterTAD", "Cworld",
                    "deDoc", "domaincaller", "HiCExplorer", "HiCseg", "ICFinder", "MSTD", "OnTAD",
                    "rGMAP", "SpectralTAD", "TADLib", "TopDom"));
        else if (tableName.equals("excel_data_overview"))
            orderList = new ArrayList<>(Arrays.asList("Resolution",
                    "Total_pairs",
                    "valid_interaction",
                    "cis_interaction",
                    "Project ID",
                    "Phylum",
                    "Class",
                    "Order",
                    "Family",
                    "Genus",
                    "Specie",
                    "Cultivar",
                    "Tissue",
                    "SRA ID",
                    "Institute",
                    "Enzyme digestion",
                    "Article title"));
        else
            orderList = new ArrayList<>();
        return orderList;
    }

    private List<? extends Object> getExcelDataFromDB(String tableName, Integer pageNow, Integer pageSize) throws QueryException {


        String s = domain_to_table.get(tableName);
        if (s == null) {
            throw new RuntimeException("你输入的表不存在");
        } else if (s.equals("ExcelComparation")) {
            return excelMapper.getExcelTableComparation(pageNow, pageSize);
        } else if (s.equals("ExcelCompartment")) {
            return excelMapper.getExcelTableCompartment(pageNow, pageSize);
        } else if (s.equals("ExcelGenomics")) {
            return excelMapper.getExcelTableGenomics(pageNow, pageSize);
        } else if (s.equals("ExcelHiCMatrix")) {
            return excelMapper.getExcelTableHiCMatrix(pageNow, pageSize);
        } else if (s.equals("ExcelLoop")) {
            return excelMapper.getExcelTableLoop(pageNow, pageSize);
        } else if (s.equals("ExcelTad")) {
            return excelMapper.getExcelTableTad(pageNow, pageSize);
        } else if (s.equals("ExcelDataOverview")) {
            return excelMapper.getExcelDataOverview(pageNow, pageSize);
        } else {
            throw new QueryException("你输入的表不存在");
        }
    }

    private List<? extends Object> searchExcelDataFromDB(String tableName, String searchParam, Integer pageNow, Integer pageSize) throws QueryException {
        String s = domain_to_table.get(tableName);
        if (s == null) {
            throw new QueryException("你输入的表不存在");
        } else if (s.equals("ExcelComparation")) {
            return excelMapper.searchExcelTableComparation(searchParam, pageNow, pageSize);
        } else if (s.equals("ExcelCompartment")) {
            return excelMapper.searchExcelTableCompartment(searchParam, pageNow, pageSize);
        } else if (s.equals("ExcelGenomics")) {
            return excelMapper.searchExcelTableGenomics(searchParam, pageNow, pageSize);
        } else if (s.equals("ExcelHiCMatrix")) {
            return excelMapper.searchExcelTableHiCMatrix(searchParam, pageNow, pageSize);
        } else if (s.equals("ExcelLoop")) {
            return excelMapper.searchExcelTableLoop(searchParam, pageNow, pageSize);
        } else if (s.equals("ExcelTad")) {
            return excelMapper.searchExcelTableTad(searchParam, pageNow, pageSize);
        } else if (s.equals("ExcelDataOverview")) {
            return excelMapper.searchExcelDataOverview(searchParam, pageNow, pageSize);
        } else {
            throw new QueryException("你输入的表不存在");
        }
    }

    @Override
    public List<Object> getExcelData(String tableName, Integer pageNow, Integer pageSize) throws QueryException {
        List<Object> domain_list = new ArrayList<>();
        domain_list.add(getExcelOrder(tableName));
        domain_list.addAll(getExcelDataFromDB(tableName, pageNow, pageSize));
        return domain_list;
    }

    @Override
    public List<Object> searchExcelData(String tableName, String searchParam, Integer pageNow, Integer pageSize) throws QueryException {
        ArrayList<Object> domain_list = new ArrayList<>();
        domain_list.add(getExcelOrder(tableName));
        domain_list.addAll(searchExcelDataFromDB(tableName, searchParam, pageNow, pageSize));
        return domain_list;
    }

    @Override
    public Integer getExcelDataCount(String tableName) throws QueryException {
        String s = domain_to_table.get(tableName);
        if (s == null) {
            throw new QueryException("你输入的表不存在");
        } else {
            return excelMapper.getExcelTableCount(tableName);
        }
    }
}
