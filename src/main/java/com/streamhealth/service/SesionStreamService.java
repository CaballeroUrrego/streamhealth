package com.streamhealth.service;

import com.streamhealth.entity.PausaRealizada;
import com.streamhealth.entity.SesionStream;
import com.streamhealth.entity.SesionStream.EstadoSesion;
import com.streamhealth.entity.Usuario;
import com.streamhealth.repository.PausaRealizadaRepository;
import com.streamhealth.repository.SesionStreamRepository;
import com.streamhealth.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SesionStreamService {

    private final SesionStreamRepository sesionRepository;
    private final PausaRealizadaRepository pausaRepository;
    private final UsuarioRepository usuarioRepository;

    public SesionStreamService(
            SesionStreamRepository sesionRepository,
            PausaRealizadaRepository pausaRepository,
            UsuarioRepository usuarioRepository) {
        this.sesionRepository = sesionRepository;
        this.pausaRepository = pausaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public SesionStream iniciarSesion(Long idUsuario) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado: " + idUsuario));

        SesionStream sesion = new SesionStream();
        sesion.setUsuario(usuario);
        sesion.setFechaInicio(LocalDateTime.now());
        sesion.setTiempoStreamMin(0);
        sesion.setTiempoTotalSentadoMin(0);
        sesion.setEstado(EstadoSesion.ACTIVA);
        return sesionRepository.save(sesion);
    }

    @Transactional
    public SesionStream finalizarSesion(Long idSesion, Integer tiempoSentadoMin) {
        SesionStream sesion = sesionRepository.findById(idSesion)
                .orElseThrow(() -> new IllegalArgumentException("Sesión no encontrada: " + idSesion));

        LocalDateTime fin = LocalDateTime.now();
        sesion.setFechaFin(fin);
        sesion.setEstado(EstadoSesion.FINALIZADA);

        long minutosTranscurridos = Math.max(0, Duration.between(sesion.getFechaInicio(), fin).toMinutes());
        sesion.setTiempoStreamMin((int) minutosTranscurridos);

        if (tiempoSentadoMin != null && tiempoSentadoMin >= 0) {
            sesion.setTiempoTotalSentadoMin(tiempoSentadoMin);
        } else {
            sesion.setTiempoTotalSentadoMin((int) minutosTranscurridos);
        }
        return sesionRepository.save(sesion);
    }

    @Transactional
    public PausaRealizada registrarBreakChallenge(
            Long idSesion,
            Long idUsuario,
            Integer duracionMin,
            String tipoPausa,
            Integer xpGanados) {

        SesionStream sesion = sesionRepository.findById(idSesion)
                .orElseThrow(() -> new IllegalArgumentException("Sesión no encontrada: " + idSesion));
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado: " + idUsuario));

        PausaRealizada pausa = new PausaRealizada();
        pausa.setSesion(sesion);
        pausa.setUsuario(usuario);
        pausa.setFechaHora(LocalDateTime.now());
        pausa.setDuracionMin(duracionMin != null && duracionMin > 0 ? duracionMin : 3);
        pausa.setTipoPausa(tipoPausa != null && !tipoPausa.isBlank() ? tipoPausa : "Break Challenge (Caminar / Moverse / Hidratarse)");
        pausa.setXpGanados(xpGanados != null && xpGanados > 0 ? xpGanados : 50);
        pausa.setCompletada(true);

        return pausaRepository.save(pausa);
    }

    public Map<String, Object> resumenProgresoUsuario(Long idUsuario) {
        List<PausaRealizada> pausas = pausaRepository.findAll().stream()
                .filter(p -> p.getUsuario() != null && idUsuario.equals(p.getUsuario().getIdUsuario()) && Boolean.TRUE.equals(p.getCompletada()))
                .toList();

        int totalPausas = pausas.size();
        int totalXp = pausas.stream().mapToInt(p -> p.getXpGanados() != null ? p.getXpGanados() : 0).sum();
        int minutosMovimiento = pausas.stream().mapToInt(p -> p.getDuracionMin() != null ? p.getDuracionMin() : 0).sum();
        int nivel = (totalXp / 150) + 1;

        Map<String, Object> resumen = new HashMap<>();
        resumen.put("idUsuario", idUsuario);
        resumen.put("nivel", nivel);
        resumen.put("xpTotal", totalXp);
        resumen.put("pausasCompletadas", totalPausas);
        resumen.put("minutosMovimiento", minutosMovimiento);
        return resumen;
    }
}
