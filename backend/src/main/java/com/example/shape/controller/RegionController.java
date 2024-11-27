package com.example.shape.controller;

import com.example.shape.entity.RegionEntity;
import com.example.shape.service.RegionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api")
public class RegionController {

    @Autowired
    private RegionService regionService;

    @GetMapping("/regions")
    public List<RegionEntity> getAllRegions() {
        return regionService.getAllRegions();
    }

    @GetMapping("/regions/{id}")
    public RegionEntity getRegionById(@PathVariable String id) {
        return regionService.getRegionById(Long.valueOf(id));
    }
}
