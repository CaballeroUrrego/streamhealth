package com.streamhealth.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "registro_cafeina")
public class RegistroCafeina {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cafeina")
    private Long idCafeina;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_sesion")
    private SesionStream sesion;

    @Column(name = "fecha_hora")
    private LocalDateTime fechaHora;

    @Column(nullable = false, length = 100)
    private String bebida;

    @Column(name = "volumen_ml", nullable = false)
    private Integer volumenMl;

    @Column(name = "cafeina_mg", nullable = false, precision = 6, scale = 2)
    private BigDecimal cafeinaMg;

    public Long getIdCafeina() { return idCafeina; }
    public void setIdCafeina(Long idCafeina) { this.idCafeina = idCafeina; }
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
    public SesionStream getSesion() { return sesion; }
    public void setSesion(SesionStream sesion) { this.sesion = sesion; }
    public LocalDateTime getFechaHora() { return fechaHora; }
    public void setFechaHora(LocalDateTime fechaHora) { this.fechaHora = fechaHora; }
    public String getBebida() { return bebida; }
    public void setBebida(String bebida) { this.bebida = bebida; }
    public Integer getVolumenMl() { return volumenMl; }
    public void setVolumenMl(Integer volumenMl) { this.volumenMl = volumenMl; }
    public BigDecimal getCafeinaMg() { return cafeinaMg; }
    public void setCafeinaMg(BigDecimal cafeinaMg) { this.cafeinaMg = cafeinaMg; }
}
