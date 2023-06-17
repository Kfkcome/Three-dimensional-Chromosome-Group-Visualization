package com.feidian.ChromosView.controller;

import com.feidian.ChromosView.domain.ChromosomeT;
import com.feidian.ChromosView.domain.Cultivar;
import com.feidian.ChromosView.domain.Species;
import com.feidian.ChromosView.exception.QueryException;
import com.feidian.ChromosView.service.CultivarService;
import com.feidian.ChromosView.service.SpeciesService;
import com.feidian.ChromosView.utils.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/option")
public class OptionController {
    private final SpeciesService speciesService;
    private final CultivarService cultivarService;
    @Autowired
    OptionController(SpeciesService speciesService, CultivarService cultivarService)
    {
        this.speciesService = speciesService;
        this.cultivarService = cultivarService;
    }
    @GetMapping("/species")
    ApiResponse findSpecies(){
        List<Species> allSpecies = speciesService.findALLSpecies();
        return ApiResponse.success(allSpecies);
    }
    @GetMapping("/cultivar/all")
    ApiResponse findCultivar(){
        List<Cultivar> allCultivar = cultivarService.findAllCultivar();
        return ApiResponse.success(allCultivar);
    }
    @GetMapping("/cultivar/id/{SpeciesID}")
    ApiResponse findCultivarBySpeciesID(@PathVariable(value ="SpeciesID" ) Integer SpeciesID) throws QueryException {
        List<Cultivar> cultivarBySpeciesID = cultivarService.findCultivarBySpeciesID(SpeciesID);
        if(cultivarBySpeciesID.isEmpty())
        {
            throw new QueryException("查询不到数据");
        }
        return ApiResponse.success(cultivarBySpeciesID);
    }
    @GetMapping("/chromosome/all")
    ApiResponse findAllChromosome(){
        List<ChromosomeT> allChromosomeT = cultivarService.findAllChromosome();
        return ApiResponse.success(allChromosomeT);
    }
    @GetMapping("/chromosome/id/{CultivarID}")
    ApiResponse findChromosomeByID(@PathVariable(value = "CultivarID") Integer CultivarID) throws QueryException {
        List<ChromosomeT> csByID = cultivarService.findCSByID(CultivarID);
        if(csByID.isEmpty())
        {
            throw new QueryException("查询不到数据");
        }
        return ApiResponse.success(csByID);
    }
}
