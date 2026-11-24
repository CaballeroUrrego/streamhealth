package com.streamhealth.dto;

import com.streamhealth.entity.SesionStream;
import java.time.LocalDateTime;

public record SesionStreamResponse(
        Long idSesion,
        Long idUsuario,
        LocalDateTime fechaInicio,
        LocalDateTime fechaFin,
        Integer tiempoTotalSentadoMin,
        Integer tiempoStreamMin,
        SesionStream.EstadoSesion estado
) {
    public static SesionStreamResponse fromEntity(SesionStream s) {
        Long idUsuario = s.getUsuario() != null ? s.getUsuario().getIdUsuario() : null;
        return new SesionStreamResponse(
                s.getIdSesion(),
                idUsuario,
                s.getFechaInicio(),
                s.getFechaFin(),
                s.getTiempoTotalSentadoMin(),
                s.getTiempoStreamMin(),
                s.getEstado()
        );
    }
}