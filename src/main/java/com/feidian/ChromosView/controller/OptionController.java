package com.feidian.ChromosView.controller;

import com.feidian.ChromosView.domain.*;
import com.feidian.ChromosView.exception.QueryException;
import com.feidian.ChromosView.log.LogPrint;
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
    ApiResponse<List<Species>> findSpecies() {
        List<Species> allSpecies = speciesService.findALLSpecies();
        return ApiResponse.success(allSpecies);
    }

    @LogPrint
    @GetMapping("/cultivar/all")
    ApiResponse<List<Cultivar>> findCultivar() {
        List<Cultivar> allCultivar = cultivarService.findAllCultivar();
        return ApiResponse.success(allCultivar);
    }

    @LogPrint
    @GetMapping("/cultivar/id/{SpeciesID}")
    ApiResponse<List<Cultivar>> findCultivarBySpeciesID(@PathVariable(value = "SpeciesID") Integer SpeciesID) throws QueryException {
        List<Cultivar> cultivarBySpeciesID = cultivarService.findCultivarBySpeciesID(SpeciesID);
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
    @GetMapping("/chromosome/id/{CultivarID}")
    ApiResponse<List<ChromosomeT>> findChromosomeByID(@PathVariable(value = "CultivarID") Integer CultivarID) throws QueryException {
        List<ChromosomeT> csByID = cultivarService.findCSByID(CultivarID);
        if (csByID.isEmpty()) {
            throw new QueryException("查询不到数据");
        }
        return ApiResponse.success(csByID);
    }

    @GetMapping("/tissue/{CultivarID}")
    ApiResponse<List<Tissue>> findTissueByCultivar(@PathVariable Integer CultivarID) {
        List<Tissue> tissueByID = cultivarService.findTissueByID(CultivarID);
        if (tissueByID == null) {
            return ApiResponse.fail(202,"查询不到数据");
        }
        return ApiResponse.success(tissueByID);
    }
    @GetMapping("/software/all")
    ApiResponse<List<Software>>findAllSoftware(){
        return ApiResponse.success(cultivarService.findAllSoftware());
    }
}
