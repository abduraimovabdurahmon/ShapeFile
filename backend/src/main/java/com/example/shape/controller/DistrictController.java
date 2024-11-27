package com.example.shape.controller;

import com.example.shape.entity.DistrictEntity;
import com.example.shape.service.DistrictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api")
public class DistrictController {
    @Autowired
    private DistrictService districtService;

    @GetMapping("/districts")
    public List<DistrictEntity> getAllDistricts() {
        return districtService.getAllDistricts();
    }

    @GetMapping("/districts/{id}")
    public DistrictEntity getDistrictById(@PathVariable String id) {
        return districtService.getDistrictById(Long.valueOf(id));
    }

    @GetMapping("/regions/{id}/districts")
    public List<DistrictEntity> getDistrictsByRegionId(@PathVariable String id) {
        return districtService.getDistrictsByRegionId(Long.valueOf(id));
    }
}
