package com.streamhealth.controller;

import com.streamhealth.dto.SuenoResponse;
import com.streamhealth.service.SuenoService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/sueno")
@CrossOrigin(origins = "*")
public class SuenoController {

    private final SuenoService suenoService;

    public SuenoController(SuenoService suenoService) {
        this.suenoService = suenoService;
    }

    @PostMapping
    public ResponseEntity<SuenoResponse> registrar(
            @RequestParam Long idUsuario,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaRegistro,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime horaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime horaFin,
            @RequestParam(required = false) BigDecimal horasDormidas,
            @RequestParam(required = false) Integer calidadPercibida) {
        SuenoResponse creado = suenoService.registrarSueno(
                idUsuario, fechaRegistro, horaInicio, horaFin, horasDormidas, calidadPercibida);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<SuenoResponse>> historial(@PathVariable Long idUsuario) {
        return ResponseEntity.ok(suenoService.listarPorUsuario(idUsuario));
    }
}