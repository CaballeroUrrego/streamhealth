package com.streamhealth.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public record BienestarResumenResponse(
        Long idUsuario,
        Map<String, Integer> dimensionesPorcentaje,
        int indiceHabitosGeneral,
        int totalSesiones,
        int totalPausasCompletadas,
        int minutosMovimiento,
        BigDecimal totalCafeinaMg,
        BigDecimal promedioHorasSueno,
        List<String> patronesDetectados,
        List<String> sugerenciasHabitos,
        String notaEnfoque
) {}