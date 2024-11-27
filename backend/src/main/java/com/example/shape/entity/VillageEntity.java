package com.example.shape.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "village")
@Getter
@Setter
public class VillageEntity {
    @Id
    private Long id;

    @Column(name = "name_uz")
    private String nameUz;

    @Column(name = "name_oz")
    private String nameOz;

    @Column(name = "name_ru")
    private String nameRu;

    @ManyToOne
    @JoinColumn(name = "district_id")
    private DistrictEntity district;
}

