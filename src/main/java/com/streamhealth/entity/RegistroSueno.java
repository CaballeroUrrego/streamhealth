package com.streamhealth.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "registro_sueno")
public class RegistroSueno {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_sueno")
    private Long idSueno;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @Column(name = "fecha_registro", nullable = false)
    private LocalDate fechaRegistro;

    @Column(name = "hora_inicio", nullable = false)
    private LocalDateTime horaInicio;

    @Column(name = "hora_fin", nullable = false)
    private LocalDateTime horaFin;

    @Column(name = "horas_dormidas", nullable = false, precision = 4, scale = 2)
    private BigDecimal horasDormidas;

    @Column(name = "calidad_percibida")
    private Integer calidadPercibida;

    public Long getIdSueno() { return idSueno; }
    public void setIdSueno(Long idSueno) { this.idSueno = idSueno; }
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
    public LocalDate getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(LocalDate fechaRegistro) { this.fechaRegistro = fechaRegistro; }
    public LocalDateTime getHoraInicio() { return horaInicio; }
    public void setHoraInicio(LocalDateTime horaInicio) { this.horaInicio = horaInicio; }
    public LocalDateTime getHoraFin() { return horaFin; }
    public void setHoraFin(LocalDateTime horaFin) { this.horaFin = horaFin; }
    public BigDecimal getHorasDormidas() { return horasDormidas; }
    public void setHorasDormidas(BigDecimal horasDormidas) { this.horasDormidas = horasDormidas; }
    public Integer getCalidadPercibida() { return calidadPercibida; }
    public void setCalidadPercibida(Integer calidadPercibida) { this.calidadPercibida = calidadPercibida; }
}
