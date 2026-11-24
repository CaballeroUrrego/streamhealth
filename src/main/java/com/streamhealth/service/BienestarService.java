package com.streamhealth.service;

import com.streamhealth.dto.BienestarResumenResponse;
import com.streamhealth.entity.PausaRealizada;
import com.streamhealth.entity.RegistroCafeina;
import com.streamhealth.entity.RegistroSueno;
import com.streamhealth.entity.SesionStream;
import com.streamhealth.repository.PausaRealizadaRepository;
import com.streamhealth.repository.RegistroCafeinaRepository;
import com.streamhealth.repository.RegistroSuenoRepository;
import com.streamhealth.repository.SesionStreamRepository;
import com.streamhealth.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class BienestarService {

    private final UsuarioRepository usuarioRepository;
    private final SesionStreamRepository sesionStreamRepository;
    private final PausaRealizadaRepository pausaRealizadaRepository;
    private final RegistroCafeinaRepository cafeinaRepository;
    private final RegistroSuenoRepository suenoRepository;

    public BienestarService(
            UsuarioRepository usuarioRepository,
            SesionStreamRepository sesionStreamRepository,
            PausaRealizadaRepository pausaRealizadaRepository,
            RegistroCafeinaRepository cafeinaRepository,
            RegistroSuenoRepository suenoRepository) {
        this.usuarioRepository = usuarioRepository;
        this.sesionStreamRepository = sesionStreamRepository;
        this.pausaRealizadaRepository = pausaRealizadaRepository;
        this.cafeinaRepository = cafeinaRepository;
        this.suenoRepository = suenoRepository;
    }

    @Transactional(readOnly = true)
    public BienestarResumenResponse generarResumen(Long idUsuario) {
        if (!usuarioRepository.existsById(idUsuario)) {
            throw new IllegalArgumentException("Usuario no encontrado: " + idUsuario);
        }

        List<SesionStream> sesiones = sesionStreamRepository.findAll().stream()
                .filter(s -> s.getUsuario() != null && idUsuario.equals(s.getUsuario().getIdUsuario()))
                .toList();

        List<PausaRealizada> pausas = pausaRealizadaRepository.findAll().stream()
                .filter(p -> p.getUsuario() != null && idUsuario.equals(p.getUsuario().getIdUsuario()))
                .filter(p -> Boolean.TRUE.equals(p.getCompletada()))
                .toList();

        List<RegistroCafeina> consumosCafeina = cafeinaRepository.findAll().stream()
                .filter(c -> c.getUsuario() != null && idUsuario.equals(c.getUsuario().getIdUsuario()))
                .toList();

        List<RegistroSueno> registrosSueno = suenoRepository.findAll().stream()
                .filter(r -> r.getUsuario() != null && idUsuario.equals(r.getUsuario().getIdUsuario()))
                .toList();

        int totalSesiones = sesiones.size();
        int totalPausas = pausas.size();
        int minutosMovimiento = pausas.stream()
                .mapToInt(p -> p.getDuracionMin() != null ? p.getDuracionMin() : 0)
                .sum();

        int sesionesProlongadasSinPausas = (int) sesiones.stream()
                .filter(s -> {
                    int streamMin = s.getTiempoStreamMin() != null ? s.getTiempoStreamMin() : 0;
                    int sentadoMin = s.getTiempoTotalSentadoMin() != null ? s.getTiempoTotalSentadoMin() : 0;
                    int duracionRef = Math.max(streamMin, sentadoMin);
                    long pausasEnSesion = pausas.stream()
                            .filter(p -> p.getSesion() != null && s.getIdSesion().equals(p.getSesion().getIdSesion()))
                            .count();
                    return duracionRef >= 180 && pausasEnSesion == 0;
                })
                .count();

        BigDecimal totalCafeinaMg = consumosCafeina.stream()
                .map(c -> c.getCafeinaMg() != null ? c.getCafeinaMg() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        long consumosTardiosCafeina = consumosCafeina.stream()
                .filter(c -> c.getFechaHora() != null && c.getFechaHora().getHour() >= 18)
                .count();

        BigDecimal promedioHorasSueno = BigDecimal.ZERO;
        if (!registrosSueno.isEmpty()) {
            BigDecimal sumaHoras = registrosSueno.stream()
                    .map(r -> r.getHorasDormidas() != null ? r.getHorasDormidas() : BigDecimal.ZERO)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            promedioHorasSueno = sumaHoras.divide(
                    BigDecimal.valueOf(registrosSueno.size()), 2, RoundingMode.HALF_UP);
        }

        // Dimensiones porcentuales del indicador de habitos (0 a 100)
        int movimientoPct = clampPct(minutosMovimiento * 5); // 20 min acumulados -> 100%
        int pausasPct = totalSesiones == 0
                ? clampPct(totalPausas * 25)
                : clampPct((int) Math.round((totalPausas * 100.0) / Math.max(1, totalSesiones * 2.0)));

        int suenoPct;
        if (registrosSueno.isEmpty()) {
            suenoPct = 50;
        } else {
            double horas = promedioHorasSueno.doubleValue();
            if (horas >= 7.0 && horas <= 9.0) {
                suenoPct = 90;
            } else if (horas >= 6.0) {
                suenoPct = 70;
            } else {
                suenoPct = 45;
            }
        }

        int cafeinaPct;
        if (consumosCafeina.isEmpty()) {
            cafeinaPct = 80;
        } else {
            int penalizacionTardia = (int) (consumosTardiosCafeina * 15);
            int penalizacionAlta = totalCafeinaMg.doubleValue() > 400.0 ? 20 : 0;
            cafeinaPct = clampPct(95 - penalizacionTardia - penalizacionAlta);
        }

        Map<String, Integer> dimensiones = new LinkedHashMap<>();
        dimensiones.put("movimiento", movimientoPct);
        dimensiones.put("pausas", pausasPct);
        dimensiones.put("sueno", suenoPct);
        dimensiones.put("cafeina", cafeinaPct);

        int indiceGeneral = (int) Math.round(
                (movimientoPct + pausasPct + suenoPct + cafeinaPct) / 4.0);

        List<String> patrones = new ArrayList<>();
        List<String> sugerencias = new ArrayList<>();

        if (sesionesProlongadasSinPausas > 0) {
            patrones.add("Se detectaron " + sesionesProlongadasSinPausas
                    + " sesion(es) de 180+ min sin pausas registradas.");
            sugerencias.add("Configura recordatorios de Break Challenge de 3 minutos para moverte e hidratarte durante transmisiones largas.");
        }

        if (!registrosSueno.isEmpty() && promedioHorasSueno.doubleValue() < 6.5) {
            patrones.add("Promedio de sueno registrado por debajo de 6.5h (" + promedioHorasSueno + "h).");
            sugerencias.add("Revisa tus horarios de cierre de stream y descanso para proteger tu ventana de sueno.");
        }

        if (consumosTardiosCafeina > 0) {
            patrones.add("Se registraron " + consumosTardiosCafeina
                    + " consumo(s) de cafeina despues de las 18:00.");
            sugerencias.add("Observa como los consumos en horario vespertino/nocturno coinciden con tus horas y calidad de sueno.");
        }

        if (patrones.isEmpty()) {
            patrones.add("Aun no se observan patrones de riesgo acumulados en los registros actuales.");
            sugerencias.add("Continua registrando pausas, descanso y cafeina para enriquecer tus tendencias semanales.");
        }

        String notaEnfoque = "Indicador de habitos y tendencias de StreamHealth con fines informativos; no constituye diagnostico medico.";

        return new BienestarResumenResponse(
                idUsuario,
                dimensiones,
                indiceGeneral,
                totalSesiones,
                totalPausas,
                minutosMovimiento,
                totalCafeinaMg,
                promedioHorasSueno,
                patrones,
                sugerencias,
                notaEnfoque
        );
    }

    private int clampPct(int valor) {
        return Math.max(0, Math.min(100, valor));
    }
}