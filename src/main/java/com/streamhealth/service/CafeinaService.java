package com.streamhealth.service;

import com.streamhealth.dto.CafeinaResponse;
import com.streamhealth.entity.RegistroCafeina;
import com.streamhealth.entity.SesionStream;
import com.streamhealth.entity.Usuario;
import com.streamhealth.repository.RegistroCafeinaRepository;
import com.streamhealth.repository.SesionStreamRepository;
import com.streamhealth.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CafeinaService {

    private final RegistroCafeinaRepository cafeinaRepository;
    private final UsuarioRepository usuarioRepository;
    private final SesionStreamRepository sesionStreamRepository;

    public CafeinaService(
            RegistroCafeinaRepository cafeinaRepository,
            UsuarioRepository usuarioRepository,
            SesionStreamRepository sesionStreamRepository) {
        this.cafeinaRepository = cafeinaRepository;
        this.usuarioRepository = usuarioRepository;
        this.sesionStreamRepository = sesionStreamRepository;
    }

    @Transactional
    public CafeinaResponse registrarConsumo(
            Long idUsuario,
            Long idSesion,
            String bebida,
            Integer volumenMl,
            BigDecimal cafeinaMg) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado: " + idUsuario));

        SesionStream sesion = null;
        if (idSesion != null) {
            sesion = sesionStreamRepository.findById(idSesion)
                    .orElseThrow(() -> new IllegalArgumentException("Sesion no encontrada: " + idSesion));
        }

        RegistroCafeina registro = new RegistroCafeina();
        registro.setUsuario(usuario);
        registro.setSesion(sesion);
        registro.setBebida(bebida);
        registro.setVolumenMl(volumenMl);
        registro.setCafeinaMg(cafeinaMg);

        RegistroCafeina guardada = cafeinaRepository.saveAndFlush(registro);
        return toResponse(guardada);
    }

    @Transactional(readOnly = true)
    public List<CafeinaResponse> listarPorUsuario(Long idUsuario) {
        if (!usuarioRepository.existsById(idUsuario)) {
            throw new IllegalArgumentException("Usuario no encontrado: " + idUsuario);
        }
        return cafeinaRepository.findAll().stream()
                .filter(r -> r.getUsuario() != null && idUsuario.equals(r.getUsuario().getIdUsuario()))
                .map(this::toResponse)
                .toList();
    }

    private CafeinaResponse toResponse(RegistroCafeina r) {
        Long idUsuario = r.getUsuario() != null ? r.getUsuario().getIdUsuario() : null;
        Long idSesion = r.getSesion() != null ? r.getSesion().getIdSesion() : null;
        return new CafeinaResponse(
                r.getIdCafeina(),
                idUsuario,
                idSesion,
                r.getFechaHora(),
                r.getBebida(),
                r.getVolumenMl(),
                r.getCafeinaMg()
        );
    }
}