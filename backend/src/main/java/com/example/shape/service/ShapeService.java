package com.example.shape.service;


import com.example.shape.dto.ShapeDTO;
import com.example.shape.dto.enums.UsageType;
import com.example.shape.entity.ShapeEntity;
import com.example.shape.repository.ShapeRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.persistence.EntityManager;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class ShapeService {

    @Autowired
    private ShapeRepository shapeRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private EntityManager entityManager;


    public void saveShape(String name, String ownerName, Geometry geometry, UsageType usageType, Long villageId) throws ParseException {
        WKTReader wktReader = new WKTReader();

        Geometry newGeometry = wktReader.read(geometry.toText());

        ShapeEntity shapeEntity = new ShapeEntity();
        shapeEntity.setName(name);
        shapeEntity.setOwnerName(ownerName);
        shapeEntity.setUsageType(usageType);
        shapeEntity.setVillageId(villageId);
        shapeEntity.setGeometry(newGeometry);

        ShapeEntity savedShape = shapeRepository.save(shapeEntity);

        Long id = savedShape.getId();

        shapeRepository.updateGeometry(id, geometry.toText());
    }


    // get all shape by village id
    public List<ShapeDTO> getAllShapesByVillageId(Long villageId) throws JsonProcessingException {
        List<String> shapes = shapeRepository.getAllShapesByVillageId(villageId);
        List<ShapeDTO> shapeDTOS = new ArrayList<>();

        for (String shape : shapes) {
            ObjectNode shapeNode = (ObjectNode) objectMapper.readTree(shape);
            String geoJsonString = shapeNode.get("geoJSON").asText();
            JsonNode geoJsonNode = objectMapper.readTree(geoJsonString);
            shapeNode.set("geoJSON", geoJsonNode);
            ShapeDTO shapeDTO = objectMapper.treeToValue(shapeNode, ShapeDTO.class);
            shapeDTOS.add(shapeDTO);
        }

        return shapeDTOS;
    }

    // get shape by id
    public ShapeDTO getShapeById(Long id) throws JsonProcessingException {
        String shape = shapeRepository.getShapeById(id);

        // If shape is null, return an empty ShapeDTO
        if (shape == null) {
            return new ShapeDTO();
        }

        ObjectNode shapeNode = (ObjectNode) objectMapper.readTree(shape);
        String geoJsonString = shapeNode.get("geoJSON").asText();
        JsonNode geoJsonNode = objectMapper.readTree(geoJsonString);
        shapeNode.set("geoJSON", geoJsonNode);
        return objectMapper.treeToValue(shapeNode, ShapeDTO.class);
    }

}
