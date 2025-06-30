package com.example.capacitacionesbe.service;

import com.example.capacitacionesbe.model.Actividades;
import com.example.capacitacionesbe.model.Personas;
import com.example.capacitacionesbe.repository.ActividadesRepository;
import com.example.capacitacionesbe.repository.PersonasRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ActividadService {

    private final ActividadesRepository actividadRepository;
    private final PersonasRepository personaRepository;

    @Autowired
    public ActividadService(ActividadesRepository actividadRepository, PersonasRepository personaRepository) {
        this.actividadRepository = actividadRepository;
        this.personaRepository = personaRepository;
    }

    public List<Actividades> findAllActividades() {
        return actividadRepository.findAll();
    }

    public Optional<Actividades> findActividadById(Long id) {
        return actividadRepository.findById(id);
    }

    @Transactional
    public Actividades saveActividad(Actividades actividad) {
        // Lógica de negocio para asegurar que el responsable exista
        if (actividad.getResponsable() == null || actividad.getResponsable().getPersonaId() == null) {
            throw new IllegalArgumentException("La actividad debe tener un responsable asignado.");
        }
        Optional<Personas> responsable = personaRepository.findById(actividad.getResponsable().getPersonaId());
        if (responsable.isEmpty()) {
            throw new IllegalArgumentException("Responsable no encontrado con ID: " + actividad.getResponsable().getPersonaId());
        }
        actividad.setResponsable(responsable.get());

        return actividadRepository.save(actividad);
    }

    @Transactional
    public Actividades updateActividad(Long id, Actividades actividadDetails) {
        Optional<Actividades> existingActividad = actividadRepository.findById(id);
        if (existingActividad.isPresent()) {
            Actividades actividad = existingActividad.get();
            actividad.setNombre(actividadDetails.getNombre());
            actividad.setDescripcion(actividadDetails.getDescripcion());
            actividad.setFecha(actividadDetails.getFecha());
            actividad.setTipo(actividadDetails.getTipo());

            // Actualizar responsable
            if (actividadDetails.getResponsable() != null && actividadDetails.getResponsable().getPersonaId() != null) {
                Optional<Personas> newResponsable = personaRepository.findById(actividadDetails.getResponsable().getPersonaId());
                if (newResponsable.isEmpty()) {
                    throw new IllegalArgumentException("Nuevo responsable no encontrado con ID: " + actividadDetails.getResponsable().getPersonaId());
                }
                actividad.setResponsable(newResponsable.get());
            } else {
                throw new IllegalArgumentException("La actividad debe tener un responsable asignado para la actualización.");
            }

            return actividadRepository.save(actividad);
        } else {
            throw new RuntimeException("Actividad no encontrada con ID: " + id);
        }
    }

    public void deleteActividad(Long id) {
        if (actividadRepository.existsById(id)) {
            actividadRepository.deleteById(id);
        } else {
            throw new RuntimeException("Actividad no encontrada con ID: " + id);
        }
    }
}
