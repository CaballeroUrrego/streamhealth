package com.streamhealth.controller;

import com.streamhealth.dto.BienestarResumenResponse;
import com.streamhealth.service.BienestarService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bienestar")
@CrossOrigin(origins = "*")
public class BienestarController {

    private final BienestarService bienestarService;

    public BienestarController(BienestarService bienestarService) {
        this.bienestarService = bienestarService;
    }

    @GetMapping("/resumen/{idUsuario}")
    public ResponseEntity<BienestarResumenResponse> resumen(@PathVariable Long idUsuario) {
        return ResponseEntity.ok(bienestarService.generarResumen(idUsuario));
    }
}