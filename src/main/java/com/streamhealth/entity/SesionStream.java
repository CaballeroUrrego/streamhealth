package com.streamhealth.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "sesiones_stream")
public class SesionStream {
    public enum EstadoSesion { ACTIVA, FINALIZADA, PAUSADA }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_sesion")
    private Long idSesion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @Column(name = "fecha_inicio", nullable = false)
    private LocalDateTime fechaInicio;

    @Column(name = "fecha_fin")
    private LocalDateTime fechaFin;

    @Column(name = "tiempo_total_sentado_min")
    private Integer tiempoTotalSentadoMin = 0;

    @Column(name = "tiempo_stream_min")
    private Integer tiempoStreamMin = 0;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ENUM('ACTIVA','FINALIZADA','PAUSADA')")
    private EstadoSesion estado = EstadoSesion.ACTIVA;

    public Long getIdSesion() { return idSesion; }
    public void setIdSesion(Long idSesion) { this.idSesion = idSesion; }
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
    public LocalDateTime getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDateTime fechaInicio) { this.fechaInicio = fechaInicio; }
    public LocalDateTime getFechaFin() { return fechaFin; }
    public void setFechaFin(LocalDateTime fechaFin) { this.fechaFin = fechaFin; }
    public Integer getTiempoTotalSentadoMin() { return tiempoTotalSentadoMin; }
    public void setTiempoTotalSentadoMin(Integer tiempoTotalSentadoMin) { this.tiempoTotalSentadoMin = tiempoTotalSentadoMin; }
    public Integer getTiempoStreamMin() { return tiempoStreamMin; }
    public void setTiempoStreamMin(Integer tiempoStreamMin) { this.tiempoStreamMin = tiempoStreamMin; }
    public EstadoSesion getEstado() { return estado; }
    public void setEstado(EstadoSesion estado) { this.estado = estado; }
}
