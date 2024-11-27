package com.example.shape.repository;

import com.example.shape.entity.VillageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VillageRepository extends JpaRepository<VillageEntity, Long> {
    List<VillageEntity> findByDistrictId(Long districtId);
}
