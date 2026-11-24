package com.streamhealth.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "autorizaciones_privacidad")
public class AutorizacionPrivacidad {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_autorizacion")
    private Long idAutorizacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @Column(name = "acepto_terminos", nullable = false)
    private Boolean aceptoTerminos = false;

    @Column(name = "acepto_datos_sensibles", nullable = false)
    private Boolean aceptoDatosSensibles = false;

    @Column(name = "fecha_aceptacion")
    private LocalDateTime fechaAceptacion;

    @Column(name = "version_politica", length = 20)
    private String versionPolitica = "1.0";

    public Long getIdAutorizacion() { return idAutorizacion; }
    public void setIdAutorizacion(Long idAutorizacion) { this.idAutorizacion = idAutorizacion; }
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
    public Boolean getAceptoTerminos() { return aceptoTerminos; }
    public void setAceptoTerminos(Boolean aceptoTerminos) { this.aceptoTerminos = aceptoTerminos; }
    public Boolean getAceptoDatosSensibles() { return aceptoDatosSensibles; }
    public void setAceptoDatosSensibles(Boolean aceptoDatosSensibles) { this.aceptoDatosSensibles = aceptoDatosSensibles; }
    public LocalDateTime getFechaAceptacion() { return fechaAceptacion; }
    public void setFechaAceptacion(LocalDateTime fechaAceptacion) { this.fechaAceptacion = fechaAceptacion; }
    public String getVersionPolitica() { return versionPolitica; }
    public void setVersionPolitica(String versionPolitica) { this.versionPolitica = versionPolitica; }
}
