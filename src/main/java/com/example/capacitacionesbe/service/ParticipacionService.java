package com.example.capacitacionesbe.service;

import com.example.capacitacionesbe.model.Actividades;
import com.example.capacitacionesbe.model.Cursos;
import com.example.capacitacionesbe.model.Participaciones;
import com.example.capacitacionesbe.model.Personas;
import com.example.capacitacionesbe.repository.ActividadesRepository;
import com.example.capacitacionesbe.repository.CursosRepository;
import com.example.capacitacionesbe.repository.ParticipacionesRepository;
import com.example.capacitacionesbe.repository.PersonasRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ParticipacionService {

    private final ParticipacionesRepository participacionRepository;
    private final PersonasRepository personaRepository;
    private final CursosRepository cursoRepository;
    private final ActividadesRepository actividadRepository;

    @Autowired
    public ParticipacionService(ParticipacionesRepository participacionRepository,
                                PersonasRepository personaRepository,
                                CursosRepository cursoRepository,
                                ActividadesRepository actividadRepository) {
        this.participacionRepository = participacionRepository;
        this.personaRepository = personaRepository;
        this.cursoRepository = cursoRepository;
        this.actividadRepository = actividadRepository;
    }

    public List<Participaciones> findAllParticipaciones() {
        return participacionRepository.findAll();
    }

    public Optional<Participaciones> findParticipacionById(Long id) {
        return participacionRepository.findById(id);
    }

    @Transactional
    public Participaciones saveParticipacion(Participaciones participacion) {
        // 1. Validar y asociar Persona
        if (participacion.getPersona() == null || participacion.getPersona().getPersonaId() == null) {
            throw new IllegalArgumentException("La participación debe tener una persona asignada.");
        }
        Optional<Personas> persona = personaRepository.findById(participacion.getPersona().getPersonaId());
        if (persona.isEmpty()) {
            throw new IllegalArgumentException("Persona no encontrada con ID: " + participacion.getPersona().getPersonaId());
        }
        participacion.setPersona(persona.get());

        // 2. Validar que sea un curso O una actividad, no ambos y no ninguno
        boolean hasCurso = participacion.getCurso() != null && participacion.getCurso().getCursoId() != null;
        boolean hasActividad = participacion.getActividad() != null && participacion.getActividad().getActividadId() != null;

        if (hasCurso && hasActividad) {
            throw new IllegalArgumentException("Una participación no puede estar asociada a un curso Y una actividad al mismo tiempo.");
        }
        if (!hasCurso && !hasActividad) {
            throw new IllegalArgumentException("Una participación debe estar asociada a un curso O una actividad.");
        }

        // 3. Asociar Curso o Actividad
        if (hasCurso) {
            Optional<Cursos> curso = cursoRepository.findById(participacion.getCurso().getCursoId());
            if (curso.isEmpty()) {
                throw new IllegalArgumentException("Curso no encontrado con ID: " + participacion.getCurso().getCursoId());
            }
            participacion.setCurso(curso.get());
            participacion.setActividad(null); // Aseguramos que la actividad sea null si es un curso
        } else { // hasActividad
            Optional<Actividades> actividad = actividadRepository.findById(participacion.getActividad().getActividadId());
            if (actividad.isEmpty()) {
                throw new IllegalArgumentException("Actividad no encontrada con ID: " + participacion.getActividad().getActividadId());
            }
            participacion.setActividad(actividad.get());
            participacion.setCurso(null); // Aseguramos que el curso sea null si es una actividad
        }

        // 4. Lógica de negocio adicional (ej. validar estado o rol)
        // ...

        return participacionRepository.save(participacion);
    }

    @Transactional
    public Participaciones updateParticipacion(Long id, Participaciones participacionDetails) {
        Optional<Participaciones> existingParticipacion = participacionRepository.findById(id);
        if (existingParticipacion.isEmpty()) {
            throw new RuntimeException("Participación no encontrada con ID: " + id);
        }

        Participaciones participacion = existingParticipacion.get();

        // 1. Actualizar Persona (si se proporciona)
        if (participacionDetails.getPersona() != null && participacionDetails.getPersona().getPersonaId() != null) {
            Optional<Personas> newPersona = personaRepository.findById(participacionDetails.getPersona().getPersonaId());
            if (newPersona.isEmpty()) {
                throw new IllegalArgumentException("Nueva persona no encontrada con ID: " + participacionDetails.getPersona().getPersonaId());
            }
            participacion.setPersona(newPersona.get());
        }

        // 2. Lógica de actualización para Curso/Actividad
        boolean hasCursoDetails = participacionDetails.getCurso() != null && participacionDetails.getCurso().getCursoId() != null;
        boolean hasActividadDetails = participacionDetails.getActividad() != null && participacionDetails.getActividad().getActividadId() != null;

        if (hasCursoDetails && hasActividadDetails) {
            throw new IllegalArgumentException("Una participación no puede estar asociada a un curso Y una actividad al mismo tiempo en la actualización.");
        }

        if (hasCursoDetails) {
            Optional<Cursos> newCurso = cursoRepository.findById(participacionDetails.getCurso().getCursoId());
            if (newCurso.isEmpty()) {
                throw new IllegalArgumentException("Nuevo curso no encontrado con ID: " + participacionDetails.getCurso().getCursoId());
            }
            participacion.setCurso(newCurso.get());
            participacion.setActividad(null); // Limpiar actividad si se asocia a un curso
        } else if (hasActividadDetails) {
            Optional<Actividades> newActividad = actividadRepository.findById(participacionDetails.getActividad().getActividadId());
            if (newActividad.isEmpty()) {
                throw new IllegalArgumentException("Nueva actividad no encontrada con ID: " + participacionDetails.getActividad().getActividadId());
            }
            participacion.setActividad(newActividad.get());
            participacion.setCurso(null); // Limpiar curso si se asocia a una actividad
        } else {
            // Si en el update no se provee ni curso ni actividad, ¿qué debería pasar?
            // Podrías mantener los existentes o forzar que se desvinculen, dependiendo de tu lógica de negocio.
            // Por simplicidad, aquí mantenemos los existentes si no se especifican nuevos.
        }

        // 3. Actualizar otros atributos
        participacion.setRol(participacionDetails.getRol());
        participacion.setEstado(participacionDetails.getEstado());
        participacion.setCalificacion(participacionDetails.getCalificacion());

        return participacionRepository.save(participacion);
    }

    public void deleteParticipacion(Long id) {
        if (participacionRepository.existsById(id)) {
            participacionRepository.deleteById(id);
        } else {
            throw new RuntimeException("Participación no encontrada con ID: " + id);
        }
    }
}
