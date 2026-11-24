package com.streamhealth.controller;

import com.streamhealth.dto.SesionStreamResponse;
import com.streamhealth.entity.PausaRealizada;
import com.streamhealth.entity.SesionStream;
import com.streamhealth.service.SesionStreamService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sesiones")
@CrossOrigin(origins = "*")
public class SesionStreamController {

    private final SesionStreamService sesionStreamService;

    public SesionStreamController(SesionStreamService sesionStreamService) {
        this.sesionStreamService = sesionStreamService;
    }

    @PostMapping("/iniciar")
    public ResponseEntity<SesionStreamResponse> iniciar(@RequestParam Long idUsuario) {
        SesionStream creada = sesionStreamService.iniciarSesion(idUsuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(SesionStreamResponse.fromEntity(creada));
    }

    @PostMapping("/{idSesion}/finalizar")
    public ResponseEntity<SesionStreamResponse> finalizar(
            @PathVariable Long idSesion,
            @RequestParam(required = false) Integer tiempoSentadoMin) {
        SesionStream finalizada = sesionStreamService.finalizarSesion(idSesion, tiempoSentadoMin);
        return ResponseEntity.ok(SesionStreamResponse.fromEntity(finalizada));
    }

    @PostMapping("/{idSesion}/pausas")
    public ResponseEntity<PausaRealizada> registrarPausa(
            @PathVariable Long idSesion,
            @RequestParam Long idUsuario,
            @RequestParam(defaultValue = "3") Integer duracionMin,
            @RequestParam(defaultValue = "Break Challenge (3 min)") String tipoPausa,
            @RequestParam(defaultValue = "50") Integer xpGanados) {
        PausaRealizada pausa = sesionStreamService.registrarBreakChallenge(
                idSesion, idUsuario, duracionMin, tipoPausa, xpGanados);
        return ResponseEntity.status(HttpStatus.CREATED).body(pausa);
    }

    @GetMapping("/progreso/{idUsuario}")
    public ResponseEntity<?> progreso(@PathVariable Long idUsuario) {
        return ResponseEntity.ok(sesionStreamService.resumenProgresoUsuario(idUsuario));
    }
}