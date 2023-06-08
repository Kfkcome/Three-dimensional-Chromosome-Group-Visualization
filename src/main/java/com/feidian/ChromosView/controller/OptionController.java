package com.feidian.ChromosView.controller;

import com.feidian.ChromosView.domain.Chromosome;
import com.feidian.ChromosView.domain.Cultivar;
import com.feidian.ChromosView.domain.Species;
import com.feidian.ChromosView.service.CultivarService;
import com.feidian.ChromosView.service.SpeciesService;
import com.feidian.ChromosView.utils.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
    ApiResponse findCultivarBySpeciesID(@PathVariable(value ="SpeciesID" ) Integer SpeciesID){
        List<Cultivar> cultivarBySpeciesID = cultivarService.findCultivarBySpeciesID(SpeciesID);
        if(cultivarBySpeciesID.isEmpty())
        {
            return ApiResponse.fail(ApiResponse.fail().getCode(),"查询不到物种数据");
        }
        return ApiResponse.success(cultivarBySpeciesID);
    }
    @GetMapping("/chromosome/all")
    ApiResponse findAllChromosome(){
        List<Chromosome> allChromosome = cultivarService.findAllChromosome();
        return ApiResponse.success(allChromosome);
    }
    @GetMapping("/chromosome/id/{CultivarID}")
    ApiResponse findChromosomeByID(@PathVariable(value = "CultivarID") Integer CultivarID)
    {
        List<Chromosome> csByID = cultivarService.findCSByID(CultivarID);
        if(csByID.isEmpty())
        {
            return ApiResponse.fail(ApiResponse.fail().getCode(), "查询不到数据");
        }
        return ApiResponse.success(csByID);
    }
}
