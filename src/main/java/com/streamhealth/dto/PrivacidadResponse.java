package com.streamhealth.dto;

import java.time.LocalDateTime;

public record PrivacidadResponse(
        Long idAutorizacion,
        Long idUsuario,
        Boolean aceptoTerminos,
        Boolean aceptoDatosSensibles,
        LocalDateTime fechaAceptacion,
        String versionPolitica,
        boolean habilitadoParaHabitos
) {}