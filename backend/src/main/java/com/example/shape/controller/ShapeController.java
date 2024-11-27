package com.example.shape.controller;

import com.example.shape.dto.ShapeDTO;
import com.example.shape.dto.enums.UsageType;
import com.example.shape.repository.ShapeRepository;
import com.example.shape.service.ShapeService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.geotools.data.shapefile.files.ShpFiles;
import org.geotools.data.shapefile.shp.ShapefileReader;
import org.locationtech.jts.geom.*;
import org.locationtech.jts.geom.util.GeometryCombiner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;


@RestController
@RequestMapping("/api")
public class ShapeController {

    @Autowired
    private ShapeService shapeService;
    @Autowired
    private ShapeRepository shapeRepository;


    // upload and insert shapefile
    @CrossOrigin(origins = "http://localhost:5173")
    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file,
                                             @RequestParam("name") String name,
                                             @RequestParam("ownerName") String ownerName,
                                             @RequestParam("usageType") UsageType usageType,
                                             @RequestParam("villageId") String villageId) throws Exception {


        if (file == null || name == null || ownerName == null || usageType == null || villageId == null) {
            return ResponseEntity.badRequest().body("Fayllar to'liq emas!");
        }


        Path zipFilePath = Paths.get(System.getProperty("java.io.tmpdir"), file.getOriginalFilename());
        Path outputFolder = Paths.get(System.getProperty("java.io.tmpdir"), file.getOriginalFilename().replace(".zip", ""));

        file.transferTo(zipFilePath.toFile());

        try {
            unzipFile(zipFilePath, outputFolder);
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Faylni unzip qilishda xatolik yuz berdi!");
        }


        String shapefileExtension = ".shp";

        try {
            File shapefile = Files.walk(outputFolder)
                    .filter(Files::isRegularFile)
                    .filter(path -> path.getFileName().toString().endsWith(shapefileExtension))
                    .map(Path::toFile)
                    .findFirst().orElseThrow(() -> new Exception("Shapefile not found"));


            ShpFiles shpFiles = new ShpFiles(shapefile);


            ShapefileReader shapefileReader = new ShapefileReader(shpFiles, false, false, new GeometryFactory());

            List<Geometry> geometries = new ArrayList<>();

            while (shapefileReader.hasNext()) {
                Geometry geometry = (Geometry) shapefileReader.nextRecord().shape();
                geometries.add(geometry);
            }

            shapefileReader.close();

            GeometryCombiner geometryCombiner = new GeometryCombiner(geometries);

            Geometry combinedGeometry = geometryCombiner.combine();

            System.out.println(combinedGeometry.toText());

            shapeService.saveShape(name, ownerName, combinedGeometry, usageType, Long.valueOf(villageId));


            outputFolder.toFile().delete();
            zipFilePath.toFile().delete();


            return ResponseEntity.ok("File uploaded successfully");

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Shapefile not found");
        }
    }


    // get all shapes by village id
    @CrossOrigin(origins = "http://localhost:5173")
    @GetMapping("/villages/{villageId}/shapes")
    public ResponseEntity<List<ShapeDTO>> getAllShapesByVillageId(@PathVariable Long villageId) throws JsonProcessingException {
        return ResponseEntity.ok(shapeService.getAllShapesByVillageId(villageId));
    }

    // get shape by id
    @CrossOrigin(origins = "http://localhost:5173")
    @GetMapping("/shapes/{id}")
    public ResponseEntity<ShapeDTO> getShapeById(@PathVariable Long id) throws JsonProcessingException {
        return ResponseEntity.ok(shapeService.getShapeById(id));
    }



    @CrossOrigin(origins = "http://localhost:5173")
    @DeleteMapping("/shapes/{id}")
    public ResponseEntity<String> deleteShape(@PathVariable Long id) {
        shapeRepository.deleteById(id);
        return ResponseEntity.ok("Shape deleted successfully");
    }






    private void unzipFile(Path zipFilePath, Path outputFolder) throws IOException {
        if (!Files.exists(outputFolder)) {
            Files.createDirectories(outputFolder);
        }


        try (InputStream fis = Files.newInputStream(zipFilePath);
             ZipInputStream zis = new ZipInputStream(fis)) {

            ZipEntry zipEntry;
            while ((zipEntry = zis.getNextEntry()) != null) {
                Path newFilePath = outputFolder.resolve(zipEntry.getName());


                if (zipEntry.isDirectory()) {
                    Files.createDirectories(newFilePath);
                } else {

                    if (!Files.exists(newFilePath.getParent())) {
                        Files.createDirectories(newFilePath.getParent());
                    }

                    Files.copy(zis, newFilePath, StandardCopyOption.REPLACE_EXISTING);
                }

                zis.closeEntry();
            }
        }
    }

}
