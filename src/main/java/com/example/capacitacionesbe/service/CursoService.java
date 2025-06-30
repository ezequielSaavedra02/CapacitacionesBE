package com.example.capacitacionesbe.service;

import com.example.capacitacionesbe.model.Cursos;
import com.example.capacitacionesbe.model.Personas;
import com.example.capacitacionesbe.repository.CursosRepository;
import com.example.capacitacionesbe.repository.PersonasRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Para manejar transacciones

import java.util.List;
import java.util.Optional;

@Service
public class CursoService {

    private final CursosRepository cursoRepository;
    private final PersonasRepository personaRepository; // Necesario para gestionar al responsable

    @Autowired
    public CursoService(CursosRepository cursoRepository, PersonasRepository personaRepository) {
        this.cursoRepository = cursoRepository;
        this.personaRepository = personaRepository;
    }

    public List<Cursos> findAllCursos() {
        return cursoRepository.findAll();
    }

    public Optional<Cursos> findCursoById(Long id) {
        return cursoRepository.findById(id);
    }

    @Transactional // Asegura que toda la operación sea atómica
    public Cursos saveCurso(Cursos curso) {
        // Lógica de negocio: asegurar que el responsable exista antes de guardar el curso
        if (curso.getResponsable() == null || curso.getResponsable().getPersonaId() == null) {
            throw new IllegalArgumentException("El curso debe tener un responsable asignado.");
        }
        Optional<Personas> responsable = personaRepository.findById(curso.getResponsable().getPersonaId());
        if (responsable.isEmpty()) {
            throw new IllegalArgumentException("Responsable no encontrado con ID: " + curso.getResponsable().getPersonaId());
        }
        curso.setResponsable(responsable.get()); // Asocia la entidad Persona completa

        return cursoRepository.save(curso);
    }

    @Transactional
    public Cursos updateCurso(Long id, Cursos cursoDetails) {
        Optional<Cursos> existingCurso = cursoRepository.findById(id);
        if (existingCurso.isPresent()) {
            Cursos curso = existingCurso.get();
            curso.setNombre(cursoDetails.getNombre());
            curso.setDescripcion(cursoDetails.getDescripcion());
            curso.setDuracion(cursoDetails.getDuracion());
            curso.setModalidad(cursoDetails.getModalidad());

            // Actualizar responsable si se proporciona y existe
            if (cursoDetails.getResponsable() != null && cursoDetails.getResponsable().getPersonaId() != null) {
                Optional<Personas> newResponsable = personaRepository.findById(cursoDetails.getResponsable().getPersonaId());
                if (newResponsable.isEmpty()) {
                    throw new IllegalArgumentException("Nuevo responsable no encontrado con ID: " + cursoDetails.getResponsable().getPersonaId());
                }
                curso.setResponsable(newResponsable.get());
            } else {
                // Si se quiere desvincular o no se provee un responsable en el update,
                // puedes decidir qué hacer. Como lo hicimos obligatorio, aquí no permitiríamos null.
                throw new IllegalArgumentException("El curso debe tener un responsable asignado para la actualización.");
            }

            return cursoRepository.save(curso);
        } else {
            throw new RuntimeException("Curso no encontrado con ID: " + id);
        }
    }

    public void deleteCurso(Long id) {
        if (cursoRepository.existsById(id)) {
            cursoRepository.deleteById(id);
        } else {
            throw new RuntimeException("Curso no encontrado con ID: " + id);
        }
    }
}
