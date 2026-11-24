package com.streamhealth.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CafeinaResponse(
        Long idCafeina,
        Long idUsuario,
        Long idSesion,
        LocalDateTime fechaHora,
        String bebida,
        Integer volumenMl,
        BigDecimal cafeinaMg
) {}