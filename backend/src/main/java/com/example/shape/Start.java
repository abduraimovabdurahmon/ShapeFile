package com.example.shape;

import com.example.shape.entity.DistrictEntity;
import com.example.shape.entity.RegionEntity;
import com.example.shape.entity.VillageEntity;
import com.example.shape.repository.DistrictRepository;
import com.example.shape.repository.RegionRepository;
import com.example.shape.repository.VillageRepository;
import jakarta.annotation.PostConstruct;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileReader;

@Component
public class Start {

    @Autowired
    private RegionRepository regionRepository;

    @Autowired
    private DistrictRepository districtRepository;

    @Autowired
    private VillageRepository villageRepository;

    @PostConstruct
    public void start() {

        // Save regions from regions.json file
        if(regionRepository.count() == 0) {
            File regionJson = new File("src/main/resources/static/regions.json");
            if (regionJson.exists()) {
                JSONParser jsonParser = new JSONParser();
                try {
                    Object object = jsonParser.parse(new FileReader(regionJson));
                    JSONArray jsonArray = (JSONArray) object;
                    for (Object o : jsonArray) {
                        JSONObject jsonObject = (JSONObject) o;
                        RegionEntity regionEntity = new RegionEntity();
                        regionEntity.setId(Long.valueOf((String) jsonObject.get("id")));
                        regionEntity.setNameUz((String) jsonObject.get("name_uz"));
                        regionEntity.setNameOz((String) jsonObject.get("name_oz"));
                        regionEntity.setNameRu((String) jsonObject.get("name_ru"));
                        regionRepository.save(regionEntity);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        // Save districts from districts.json file
        if (districtRepository.count() == 0) {
            File districtJson = new File("src/main/resources/static/districts.json");
            if (districtJson.exists()) {
                JSONParser jsonParser = new JSONParser();
                try {
                    Object object = jsonParser.parse(new FileReader(districtJson));
                    JSONArray jsonArray = (JSONArray) object;
                    for (Object o : jsonArray) {
                        JSONObject jsonObject = (JSONObject) o;
                        DistrictEntity districtEntity = new DistrictEntity();
                        districtEntity.setId(Long.valueOf((String) jsonObject.get("id")));
                        districtEntity.setNameUz((String) jsonObject.get("name_uz"));
                        districtEntity.setNameOz((String) jsonObject.get("name_oz"));
                        districtEntity.setNameRu((String) jsonObject.get("name_ru"));
                        districtEntity.setRegion(regionRepository.findById(Long.valueOf((String) jsonObject.get("region_id"))).orElse(null));
                        districtRepository.save(districtEntity);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        // Save villages from villages.json file
        if (villageRepository.count() == 0) {
            File villageJson = new File("src/main/resources/static/villages.json");
            if (villageJson.exists()) {
                JSONParser jsonParser = new JSONParser();
                try {
                    Object object = jsonParser.parse(new FileReader(villageJson));
                    JSONArray jsonArray = (JSONArray) object;
                    for (Object o : jsonArray) {
                        JSONObject jsonObject = (JSONObject) o;
                        VillageEntity villageEntity = new VillageEntity();
                        villageEntity.setId(Long.valueOf((String) jsonObject.get("id")));
                        villageEntity.setNameUz((String) jsonObject.get("name_uz"));
                        villageEntity.setNameOz((String) jsonObject.get("name_oz"));
                        villageEntity.setNameRu((String) jsonObject.get("name_ru"));
                        villageEntity.setDistrict(districtRepository.findById(Long.valueOf((String) jsonObject.get("district_id"))).orElse(null));
                        villageRepository.save(villageEntity);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }


    }
}
