package com.example.shape.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "district")
@Getter
@Setter
public class DistrictEntity {
    @Id
    private Long id;

    @Column(name = "name_uz")
    private String nameUz;

    @Column(name = "name_oz")
    private String nameOz;

    @Column(name = "name_ru")
    private String nameRu;

    @ManyToOne
    @JoinColumn(name = "region_id")
    private RegionEntity region;

}