package com.streamhealth.controller;

import com.streamhealth.dto.PrivacidadResponse;
import com.streamhealth.service.PrivacidadService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/privacidad")
@CrossOrigin(origins = "*")
public class PrivacidadController {

    private final PrivacidadService privacidadService;

    public PrivacidadController(PrivacidadService privacidadService) {
        this.privacidadService = privacidadService;
    }

    @PostMapping
    public ResponseEntity<PrivacidadResponse> registrar(
            @RequestParam Long idUsuario,
            @RequestParam(defaultValue = "true") Boolean aceptoTerminos,
            @RequestParam(defaultValue = "true") Boolean aceptoDatosSensibles,
            @RequestParam(defaultValue = "1.0") String versionPolitica) {
        PrivacidadResponse creada = privacidadService.registrarAutorizacion(
                idUsuario, aceptoTerminos, aceptoDatosSensibles, versionPolitica);
        return ResponseEntity.status(HttpStatus.CREATED).body(creada);
    }

    @GetMapping("/estado/{idUsuario}")
    public ResponseEntity<PrivacidadResponse> estado(@PathVariable Long idUsuario) {
        return ResponseEntity.ok(privacidadService.obtenerEstadoActual(idUsuario));
    }

    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<PrivacidadResponse>> historial(@PathVariable Long idUsuario) {
        return ResponseEntity.ok(privacidadService.listarPorUsuario(idUsuario));
    }
}