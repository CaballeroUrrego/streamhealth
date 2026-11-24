package com.streamhealth.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record SuenoResponse(
        Long idSueno,
        Long idUsuario,
        LocalDate fechaRegistro,
        LocalDateTime horaInicio,
        LocalDateTime horaFin,
        BigDecimal horasDormidas,
        Integer calidadPercibida
) {}