package com.streamhealth.dto;

public record ProgresoUsuarioResponse(
        Long idUsuario,
        int nivel,
        int xpTotal,
        int pausasCompletadas,
        int minutosMovimiento
) {}