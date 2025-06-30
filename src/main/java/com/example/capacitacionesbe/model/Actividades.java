package com.example.capacitacionesbe.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "actividades")
public class Actividades {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long actividadId;

    @Column(nullable = false)
    private String nombre;

    @Column(length = 1000)
    private String descripcion;

    @Column(nullable = false)
    private LocalDate fecha;

    @Column(nullable = false)
    private String tipo;

    @ManyToOne
    @JoinColumn(name = "responsable_id", nullable = false)
    private Personas responsable;

    // Constructores
    public Actividades() {
    }

    public Actividades(String nombre, String descripcion, LocalDate fecha, String tipo, Personas responsable) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.tipo = tipo;
        this.responsable = responsable;
    }

    // Getters y Setters
    public Long getActividadId() {
        return actividadId;
    }

    public void setActividadId(Long actividadId) {
        this.actividadId = actividadId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Personas getResponsable() {
        return responsable;
    }

    public void setResponsable(Personas responsable) {
        this.responsable = responsable;
    }

    @Override
    public String toString() {
        return "Actividad{" +
                "actividadId=" + actividadId +
                ", nombre='" + nombre + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", fecha=" + fecha +
                ", tipo='" + tipo + '\'' +
                ", responsable=" + (responsable != null ? responsable.getNombre() + " " + responsable.getApellido() : "N/A") +
                '}';
    }
}
