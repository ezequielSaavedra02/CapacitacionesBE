package com.example.capacitacionesbe.model;

import jakarta.persistence.*;

@Entity
@Table(name = "participaciones")
public class Participaciones {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long participacionId;

    @ManyToOne
    @JoinColumn(name = "persona_id", nullable = false)
    private Personas persona;

    @ManyToOne
    @JoinColumn(name = "curso_id")
    private Cursos curso;

    @ManyToOne
    @JoinColumn(name = "actividad_id")
    private Actividades actividad;

    @Column(nullable = false)
    private String rol;

    @Column(nullable = false)
    private String estado;

    private Double calificacion;

    // Constructores
    public Participaciones() {
    }

    public Participaciones(Personas persona, Cursos curso, Actividades actividad, String rol, String estado, Double calificacion) {
        this.persona = persona;
        this.curso = curso;
        this.actividad = actividad;
        this.rol = rol;
        this.estado = estado;
        this.calificacion = calificacion;
    }

    // Getters y Setters
    public Long getParticipacionId() {
        return participacionId;
    }

    public void setParticipacionId(Long participacionId) {
        this.participacionId = participacionId;
    }

    public Personas getPersona() {
        return persona;
    }

    public void setPersona(Personas persona) {
        this.persona = persona;
    }

    public Cursos getCurso() {
        return curso;
    }

    public void setCurso(Cursos curso) {
        this.curso = curso;
    }

    public Actividades getActividad() {
        return actividad;
    }

    public void setActividad(Actividades actividad) {
        this.actividad = actividad;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Double getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(Double calificacion) {
        this.calificacion = calificacion;
    }

    @Override
    public String toString() {
        return "Participacion{" +
                "participacionId=" + participacionId +
                ", persona=" + (persona != null ? persona.getNombre() : "N/A") +
                ", curso=" + (curso != null ? curso.getNombre() : "N/A") +
                ", actividad=" + (actividad != null ? actividad.getNombre() : "N/A") +
                ", rol='" + rol + '\'' +
                ", estado='" + estado + '\'' +
                ", calificacion=" + calificacion +
                '}';
    }
}
