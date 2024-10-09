package com.feidian.ChromosView.controller;

import com.feidian.ChromosView.aop.LogPrint;
import com.feidian.ChromosView.domain.CpData;
import com.feidian.ChromosView.domain.LoopData;
import com.feidian.ChromosView.domain.TadData;
import com.feidian.ChromosView.service.ConnectionService;
import com.feidian.ChromosView.utils.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/connection")
public class ConnectionController {
    ConnectionService connectionService;

    @Autowired
    ConnectionController(ConnectionService connectionService) {
        this.connectionService = connectionService;
    }

    @LogPrint
    @GetMapping("/genus")
    ApiResponse<List<String>> getGenusName() {
        return ApiResponse.success(connectionService.getGenusName());
    }

    @LogPrint
    @GetMapping("/species_cultivar")
    ApiResponse<List<String>> getSpeciesName(String genus) {
        return ApiResponse.success(connectionService.getSpeciesName(genus));
    }

//    @LogPrint
//    @GetMapping("/cultivar")
//    ApiResponse<List<String>> getCultivarName(String genus, String species) {
//        return ApiResponse.success(connectionService.getCultivarName(genus, species));
//    }

    @LogPrint
    @GetMapping("/chromosome")
    ApiResponse<List<String>> getChromosomeName(String species, String cultivar) {
        return ApiResponse.success(connectionService.getChromosomeName(species, cultivar));
    }

    @LogPrint
    @GetMapping("/all")
    ApiResponse<Map<String, Object>> getConnectionALL(String cs_name1, String cs_name2, String target1, String target2) {
        return ApiResponse.success(connectionService.getConnectionALL(cs_name1, cs_name2, target1, target2));
    }

    @LogPrint
    @GetMapping("/range")
    ApiResponse<Map<String, Object>> getConnectionByRange(String cs_name1, String target1, String target2, Integer s1, Integer e1) {
        return ApiResponse.success(connectionService.getConnectionByRange(cs_name1, target1, target2, s1, e1));
    }

    @LogPrint
    @GetMapping("/tad/all")
    ApiResponse<List<TadData>> getTadALL(String species, String cultivar, String cs_name, String software) {
        return ApiResponse.success(connectionService.getTadALL(species, cultivar, cs_name, software));
    }

    @LogPrint
    @GetMapping("/tad/range")
    ApiResponse<List<TadData>> getTadByRange(String species, String cultivar, String cs_name, Integer s, Integer e, String software) {
        return ApiResponse.success(connectionService.getTadByRange(species, cultivar, cs_name, s, e, software));
    }

    @LogPrint
    @GetMapping("/loop/all")
    ApiResponse<List<LoopData>> getLoopALL(String species, String cultivar, String cs_name, String software) {
        return ApiResponse.success(connectionService.getLoopALL(species, cultivar, cs_name, software));
    }

    @LogPrint
    @GetMapping("/loop/range")
    ApiResponse<List<LoopData>> getLoopByRange(String species, String cultivar, String cs_name, Integer s, Integer e, String software, Double threshold) {
        return ApiResponse.success(connectionService.getLoopByRange(species, cultivar, cs_name, s, e, software, threshold));
    }

    @LogPrint
    @GetMapping("/compartment/all")
    ApiResponse<List<CpData>> getCompartmentALL(String species, String cultivar, String cs_name) {
        return ApiResponse.success(connectionService.getCompartmentALL(species, cultivar, cs_name));
    }

    @LogPrint
    @GetMapping("/compartment/range")
    ApiResponse<List<CpData>> getCompartmentByRange(String species, String cultivar, String cs_name, Integer s, Integer e) {
        return ApiResponse.success(connectionService.getCompartmentByRange(species, cultivar, cs_name, s, e));
    }

}
