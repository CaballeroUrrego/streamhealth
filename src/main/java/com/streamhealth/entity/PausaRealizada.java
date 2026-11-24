package com.streamhealth.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "pausas_realizadas")
public class PausaRealizada {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pausa")
    private Long idPausa;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_sesion", nullable = false)
    private SesionStream sesion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @Column(name = "fecha_hora")
    private LocalDateTime fechaHora;

    @Column(name = "duracion_min", nullable = false)
    private Integer duracionMin = 3;

    @Column(name = "tipo_pausa", length = 100)
    private String tipoPausa = "Pausa Activa / Movimiento";

    @Column(name = "xp_ganados")
    private Integer xpGanados = 50;

    @Column(nullable = false)
    private Boolean completada = true;

    public Long getIdPausa() { return idPausa; }
    public void setIdPausa(Long idPausa) { this.idPausa = idPausa; }
    public SesionStream getSesion() { return sesion; }
    public void setSesion(SesionStream sesion) { this.sesion = sesion; }
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
    public LocalDateTime getFechaHora() { return fechaHora; }
    public void setFechaHora(LocalDateTime fechaHora) { this.fechaHora = fechaHora; }
    public Integer getDuracionMin() { return duracionMin; }
    public void setDuracionMin(Integer duracionMin) { this.duracionMin = duracionMin; }
    public String getTipoPausa() { return tipoPausa; }
    public void setTipoPausa(String tipoPausa) { this.tipoPausa = tipoPausa; }
    public Integer getXpGanados() { return xpGanados; }
    public void setXpGanados(Integer xpGanados) { this.xpGanados = xpGanados; }
    public Boolean getCompletada() { return completada; }
    public void setCompletada(Boolean completada) { this.completada = completada; }
}
