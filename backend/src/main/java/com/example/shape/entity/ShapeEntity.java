package com.example.shape.entity;

import com.example.shape.dto.enums.UsageType;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.locationtech.jts.geom.Geometry;

import java.io.Serializable;

@Entity
@Table(name = "shape")
@Getter
@Setter
public class ShapeEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "ownerName")
    private String ownerName;

    @Column(name = "geometry", columnDefinition = "geometry")
    @JdbcTypeCode(SqlTypes.GEOMETRY)
    @JsonSubTypes.Type(value = Geometry.class, name = "geometry")
    private Geometry geometry;

    @Enumerated(EnumType.STRING)
    private UsageType usageType;

    @ManyToOne(targetEntity = VillageEntity.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "village_id")
    private VillageEntity village;

    public void setVillageId(Long villageId) {
        this.village = new VillageEntity();
        this.village.setId(villageId);
    }
}
