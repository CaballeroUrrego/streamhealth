package com.streamhealth.controller;

import com.streamhealth.dto.CafeinaResponse;
import com.streamhealth.service.CafeinaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/cafeina")
@CrossOrigin(origins = "*")
public class CafeinaController {

    private final CafeinaService cafeinaService;

    public CafeinaController(CafeinaService cafeinaService) {
        this.cafeinaService = cafeinaService;
    }

    @PostMapping
    public ResponseEntity<CafeinaResponse> registrar(
            @RequestParam Long idUsuario,
            @RequestParam(required = false) Long idSesion,
            @RequestParam String bebida,
            @RequestParam Integer volumenMl,
            @RequestParam BigDecimal cafeinaMg) {
        CafeinaResponse creada = cafeinaService.registrarConsumo(
                idUsuario, idSesion, bebida, volumenMl, cafeinaMg);
        return ResponseEntity.status(HttpStatus.CREATED).body(creada);
    }

    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<CafeinaResponse>> historial(@PathVariable Long idUsuario) {
        return ResponseEntity.ok(cafeinaService.listarPorUsuario(idUsuario));
    }
}