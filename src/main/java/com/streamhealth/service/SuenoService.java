package com.streamhealth.service;

import com.streamhealth.dto.SuenoResponse;
import com.streamhealth.entity.RegistroSueno;
import com.streamhealth.entity.Usuario;
import com.streamhealth.repository.RegistroSuenoRepository;
import com.streamhealth.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class SuenoService {

    private final RegistroSuenoRepository suenoRepository;
    private final UsuarioRepository usuarioRepository;

    public SuenoService(RegistroSuenoRepository suenoRepository, UsuarioRepository usuarioRepository) {
        this.suenoRepository = suenoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public SuenoResponse registrarSueno(
            Long idUsuario,
            LocalDate fechaRegistro,
            LocalDateTime horaInicio,
            LocalDateTime horaFin,
            BigDecimal horasDormidas,
            Integer calidadPercibida) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado: " + idUsuario));

        BigDecimal horasCalculadas = horasDormidas;
        if (horasCalculadas == null && horaInicio != null && horaFin != null) {
            long minutos = Math.max(0, Duration.between(horaInicio, horaFin).toMinutes());
            horasCalculadas = BigDecimal.valueOf(minutos)
                    .divide(BigDecimal.valueOf(60), 2, RoundingMode.HALF_UP);
        }

        RegistroSueno registro = new RegistroSueno();
        registro.setUsuario(usuario);
        registro.setFechaRegistro(fechaRegistro != null ? fechaRegistro : LocalDate.now());
        registro.setHoraInicio(horaInicio);
        registro.setHoraFin(horaFin);
        registro.setHorasDormidas(horasCalculadas != null ? horasCalculadas : BigDecimal.ZERO);
        registro.setCalidadPercibida(calidadPercibida);

        RegistroSueno guardado = suenoRepository.saveAndFlush(registro);
        return toResponse(guardado);
    }

    @Transactional(readOnly = true)
    public List<SuenoResponse> listarPorUsuario(Long idUsuario) {
        if (!usuarioRepository.existsById(idUsuario)) {
            throw new IllegalArgumentException("Usuario no encontrado: " + idUsuario);
        }
        return suenoRepository.findAll().stream()
                .filter(r -> r.getUsuario() != null && idUsuario.equals(r.getUsuario().getIdUsuario()))
                .map(this::toResponse)
                .toList();
    }

    private SuenoResponse toResponse(RegistroSueno r) {
        Long idUsuario = r.getUsuario() != null ? r.getUsuario().getIdUsuario() : null;
        return new SuenoResponse(
                r.getIdSueno(),
                idUsuario,
                r.getFechaRegistro(),
                r.getHoraInicio(),
                r.getHoraFin(),
                r.getHorasDormidas(),
                r.getCalidadPercibida()
        );
    }
}