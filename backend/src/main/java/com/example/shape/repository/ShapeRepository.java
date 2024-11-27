package com.example.shape.repository;

import com.example.shape.dto.ShapeDTO;
import com.example.shape.entity.ShapeEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShapeRepository extends JpaRepository<ShapeEntity, Long> {

    @Transactional
    @Modifying
    @Query(value = "UPDATE shape SET geometry = ST_GeomFromText(?2, 4326) WHERE id = ?1", nativeQuery = true)
    void updateGeometry(Long id, String geometry);

    // get all shapes by village id
    @Transactional
    @Modifying
    @Query(value = "SELECT jsonb_build_object('id', id, 'name', name, 'owner_name', owner_name, 'usage_type', usage_type, 'village_id', village_id, 'geoJSON', ST_AsGeoJSON(geometry)) FROM shape WHERE village_id = ?1", nativeQuery = true)
    List<String> getAllShapesByVillageId(Long villageId);


    // get shape by id
    @Query(value = "SELECT jsonb_build_object('id', id, 'name', name, 'owner_name', owner_name, 'usage_type', usage_type, 'village_id', village_id, 'geoJSON', ST_AsGeoJSON(geometry)) FROM shape WHERE id = :id", nativeQuery = true)
    String getShapeById(@Param("id") Long id);
}

