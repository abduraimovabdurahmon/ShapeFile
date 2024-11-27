package com.example.shape.controller;

import com.example.shape.dto.enums.UsageType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api")
public class usageTypeController {


    @GetMapping("/usage-types")
    public ResponseEntity<List<UsageType>> getUsageType() {
        return ResponseEntity.ok(Arrays.asList(UsageType.values()));
    }

}
