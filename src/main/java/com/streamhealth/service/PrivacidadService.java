package com.streamhealth.service;

import com.streamhealth.dto.PrivacidadResponse;
import com.streamhealth.entity.AutorizacionPrivacidad;
import com.streamhealth.entity.Usuario;
import com.streamhealth.repository.AutorizacionPrivacidadRepository;
import com.streamhealth.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
public class PrivacidadService {

    private final AutorizacionPrivacidadRepository privacidadRepository;
    private final UsuarioRepository usuarioRepository;

    public PrivacidadService(
            AutorizacionPrivacidadRepository privacidadRepository,
            UsuarioRepository usuarioRepository) {
        this.privacidadRepository = privacidadRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public PrivacidadResponse registrarAutorizacion(
            Long idUsuario,
            Boolean aceptoTerminos,
            Boolean aceptoDatosSensibles,
            String versionPolitica) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado: " + idUsuario));

        AutorizacionPrivacidad autorizacion = new AutorizacionPrivacidad();
        autorizacion.setUsuario(usuario);
        autorizacion.setAceptoTerminos(Boolean.TRUE.equals(aceptoTerminos));
        autorizacion.setAceptoDatosSensibles(Boolean.TRUE.equals(aceptoDatosSensibles));
        autorizacion.setFechaAceptacion(LocalDateTime.now());
        autorizacion.setVersionPolitica(
                (versionPolitica != null && !versionPolitica.isBlank()) ? versionPolitica : "1.0");

        AutorizacionPrivacidad guardada = privacidadRepository.saveAndFlush(autorizacion);
        return toResponse(guardada);
    }

    @Transactional(readOnly = true)
    public List<PrivacidadResponse> listarPorUsuario(Long idUsuario) {
        if (!usuarioRepository.existsById(idUsuario)) {
            throw new IllegalArgumentException("Usuario no encontrado: " + idUsuario);
        }
        return privacidadRepository.findAll().stream()
                .filter(a -> a.getUsuario() != null && idUsuario.equals(a.getUsuario().getIdUsuario()))
                .sorted(Comparator.comparing(
                        AutorizacionPrivacidad::getIdAutorizacion,
                        Comparator.nullsLast(Comparator.naturalOrder())))
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public PrivacidadResponse obtenerEstadoActual(Long idUsuario) {
        List<PrivacidadResponse> historial = listarPorUsuario(idUsuario);
        if (historial.isEmpty()) {
            return new PrivacidadResponse(
                    null,
                    idUsuario,
                    false,
                    false,
                    null,
                    "1.0",
                    false
            );
        }
        return historial.get(historial.size() - 1);
    }

    private PrivacidadResponse toResponse(AutorizacionPrivacidad a) {
        Long idUsuario = a.getUsuario() != null ? a.getUsuario().getIdUsuario() : null;
        boolean terminos = Boolean.TRUE.equals(a.getAceptoTerminos());
        boolean sensibles = Boolean.TRUE.equals(a.getAceptoDatosSensibles());
        return new PrivacidadResponse(
                a.getIdAutorizacion(),
                idUsuario,
                terminos,
                sensibles,
                a.getFechaAceptacion(),
                a.getVersionPolitica(),
                terminos && sensibles
        );
    }
}