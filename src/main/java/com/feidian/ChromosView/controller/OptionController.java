package com.feidian.ChromosView.controller;

import com.feidian.ChromosView.aop.LogPrint;
import com.feidian.ChromosView.domain.ChromosomeT;
import com.feidian.ChromosView.domain.Software;
import com.feidian.ChromosView.exception.QueryException;
import com.feidian.ChromosView.service.CultivarService;
import com.feidian.ChromosView.service.SpeciesService;
import com.feidian.ChromosView.utils.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/option")
public class OptionController {
    private final SpeciesService speciesService;
    private final CultivarService cultivarService;

    @Autowired
    OptionController(SpeciesService speciesService, CultivarService cultivarService) {
        this.speciesService = speciesService;
        this.cultivarService = cultivarService;
    }

    @LogPrint
    @GetMapping("/species")
    ApiResponse<List<String>> findSpecies() {
        List<String> allSpecies = speciesService.findALLSpecies();
        return ApiResponse.success(allSpecies);
    }

    @LogPrint
    @GetMapping("/cultivar/all")
    ApiResponse<List<String>> findCultivar() {
        List<String> allCultivar = cultivarService.findAllCultivar();
        return ApiResponse.success(allCultivar);
    }

    @LogPrint
    @GetMapping("/cultivar/species/{SpeciesName}")
    ApiResponse<List<String>> findCultivarBySpeciesName(@PathVariable(value = "SpeciesName") String SpeciesName) throws QueryException {
        List<String> cultivarBySpeciesID = cultivarService.findCultivarBySpeciesName(SpeciesName);
        if (cultivarBySpeciesID.isEmpty()) {
            throw new QueryException("查询不到数据");
        }
        return ApiResponse.success(cultivarBySpeciesID);
    }

    @LogPrint
    @GetMapping("/chromosome/all")
    ApiResponse<List<ChromosomeT>> findAllChromosome() {
        List<ChromosomeT> allChromosomeT = cultivarService.findAllChromosome();
        return ApiResponse.success(allChromosomeT);
    }

    @LogPrint
    @GetMapping("/chromosome/{SpeciesName}/{CultivarName}")
    ApiResponse<List<ChromosomeT>> findChromosomeByID(@PathVariable String CultivarName, @PathVariable String SpeciesName) throws QueryException {
        List<ChromosomeT> csByID = cultivarService.findCSByCultivar(SpeciesName, CultivarName);
        if (csByID.isEmpty()) {
            throw new QueryException("查询不到数据");
        }
        return ApiResponse.success(csByID);
    }

    @LogPrint
    @GetMapping("/tissue/{SpeciesName}/{CultivarName}")
    ApiResponse<List<String>> findTissueByCultivar(@PathVariable String CultivarName, @PathVariable String SpeciesName) {
        List<String> tissueByID = cultivarService.findTissueByID(SpeciesName, CultivarName);
        if (tissueByID == null) {
            return ApiResponse.fail(202, "查询不到数据");
        }
        return ApiResponse.success(tissueByID);
    }

    @GetMapping("/software/all")
    ApiResponse<List<Software>> findAllSoftware() {
        return ApiResponse.success(cultivarService.findAllSoftware());
    }
}
