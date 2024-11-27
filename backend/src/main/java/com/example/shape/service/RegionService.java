package com.example.shape.service;

import com.example.shape.entity.RegionEntity;
import com.example.shape.repository.RegionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RegionService {

    @Autowired
    private RegionRepository regionRepository;

    // get all Regions
    public List<RegionEntity> getAllRegions() {
        return regionRepository.findAll();
    }


    // get Region by id
    public RegionEntity getRegionById(Long id) {
        return regionRepository.findById(id).orElse(null);
    }

}
