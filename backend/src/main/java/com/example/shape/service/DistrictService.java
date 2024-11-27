package com.example.shape.service;

import com.example.shape.entity.DistrictEntity;
import com.example.shape.repository.DistrictRepository;
import com.example.shape.repository.RegionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DistrictService {

    @Autowired
    private DistrictRepository districtRepository;

    @Autowired
    private RegionRepository regionRepository;


    // get all Districts
    public List<DistrictEntity> getAllDistricts() {
        return districtRepository.findAll();
    }

    // get District by id
    public DistrictEntity getDistrictById(Long id) {
        return districtRepository.findById(id).orElse(null);
    }

    // get Districts by Region id
    public List<DistrictEntity> getDistrictsByRegionId(Long regionId) {
        return districtRepository.findByRegionId(regionId);
    }

}
