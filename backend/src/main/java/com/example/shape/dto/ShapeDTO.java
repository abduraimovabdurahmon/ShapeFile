package com.example.shape.dto;

import com.example.shape.dto.enums.UsageType;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ShapeDTO {

    private Long id;
    private String name;

    @JsonProperty("owner_name")
    private String ownerName;

    @JsonProperty("usage_type")
    private UsageType usageType;

    @JsonProperty("village_id")
    private Long villageId;

    @JsonProperty("geoJSON")
    private JsonNode geoJSON;

    public ShapeDTO(Long id, String name, String ownerName, UsageType usageType, Long villageId, JsonNode geoJSON) {
        this.id = id;
        this.name = name;
        this.ownerName = ownerName;
        this.usageType = usageType;
        this.villageId = villageId;
        this.geoJSON = geoJSON;
    }

    public ShapeDTO() {

    }
}