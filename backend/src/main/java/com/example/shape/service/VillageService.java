package com.example.shape.service;

import com.example.shape.entity.VillageEntity;
import com.example.shape.repository.VillageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VillageService {
    @Autowired
    private VillageRepository villageRepository;


    // get all Villages
    public List<VillageEntity> getAllVillages() {
        return villageRepository.findAll();
    }

    // get Village by id
    public VillageEntity getVillageById(Long id) {
        return villageRepository.findById(id).orElse(null);
    }


    // get Villages by District id
    public List<VillageEntity> getVillagesByDistrictId(Long districtId) {
        return villageRepository.findByDistrictId(districtId);
    }


}
