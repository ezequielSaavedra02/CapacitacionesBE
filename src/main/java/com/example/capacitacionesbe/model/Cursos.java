package com.example.capacitacionesbe.model;


import jakarta.persistence.*;

@Entity
@Table(name = "cursos")
public class Cursos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cursoId;

    @Column(nullable = false)
    private String nombre;

    @Column(length = 1000)
    private String descripcion;

    private Integer duracion;

    private String modalidad;

    @ManyToOne
    @JoinColumn(name = "responsable_id", nullable = false)
    private Personas responsable;

    // Constructores
    public Cursos() {
    }

    public Cursos(String nombre, String descripcion, Integer duracion, String modalidad, Personas responsable) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.duracion = duracion;
        this.modalidad = modalidad;
        this.responsable = responsable;
    }

    // Getters y Setters
    public Long getCursoId() {
        return cursoId;
    }

    public void setCursoId(Long cursoId) {
        this.cursoId = cursoId;
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

    public Integer getDuracion() {
        return duracion;
    }

    public void setDuracion(Integer duracion) {
        this.duracion = duracion;
    }

    public String getModalidad() {
        return modalidad;
    }

    public void setModalidad(String modalidad) {
        this.modalidad = modalidad;
    }

    public Personas getResponsable() {
        return responsable;
    }

    public void setResponsable(Personas responsable) {
        this.responsable = responsable;
    }

    @Override
    public String toString() {
        return "Curso{" +
                "cursoId=" + cursoId +
                ", nombre='" + nombre + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", duracion=" + duracion +
                ", modalidad='" + modalidad + '\'' +
                ", responsable=" + (responsable != null ? responsable.getNombre() + " " + responsable.getApellido() : "N/A") +
                '}';
    }
}