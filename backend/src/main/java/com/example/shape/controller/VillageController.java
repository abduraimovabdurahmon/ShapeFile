package com.example.shape.controller;

import com.example.shape.entity.VillageEntity;
import com.example.shape.service.VillageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api")
public class VillageController {

    @Autowired
    private VillageService villageService;


    @GetMapping("/villages")
    public List<VillageEntity> getAllVillages() {
        return villageService.getAllVillages();
    }

    @GetMapping("/villages/{id}")
    public VillageEntity getVillageById(@PathVariable String id) {
        return villageService.getVillageById(Long.valueOf(id));
    }

    @GetMapping("/districts/{id}/villages")
    public List<VillageEntity> getVillagesByDistrictId(@PathVariable String id) {
        return villageService.getVillagesByDistrictId(Long.valueOf(id));
    }


}
