package com.example.capacitacionesbe.service;

import com.example.capacitacionesbe.model.Personas;
import com.example.capacitacionesbe.repository.PersonasRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service // Marca esta clase como un componente de servicio de Spring
public class PersonaService {

    private final PersonasRepository personaRepository;

    @Autowired // Inyección de dependencia del repositorio
    public PersonaService(PersonasRepository personaRepository) {
        this.personaRepository = personaRepository;
    }

    public List<Personas> findAllPersonas() {
        return personaRepository.findAll();
    }

    public Optional<Personas> findPersonaById(Long id) {
        return personaRepository.findById(id);
    }

    public Personas savePersona(Personas persona) {
        return personaRepository.save(persona);
    }

    public Personas updatePersona(Long id, Personas personaDetails) {
        Optional<Personas> existingPersona = personaRepository.findById(id);
        if (existingPersona.isPresent()) {
            Personas persona = existingPersona.get();
            persona.setNombre(personaDetails.getNombre());
            persona.setApellido(personaDetails.getApellido());
            persona.setDni(personaDetails.getDni());
            persona.setEmail(personaDetails.getEmail());
            persona.setTipoPersona(personaDetails.getTipoPersona());
            persona.setTelefono(personaDetails.getTelefono());
            return personaRepository.save(persona);
        } else {
            throw new RuntimeException("Persona no encontrada con ID: " + id);
        }
    }

    public void deletePersona(Long id) {
        if (personaRepository.existsById(id)) {
            personaRepository.deleteById(id);
        } else {
            throw new RuntimeException("Persona no encontrada con ID: " + id);
        }
    }

}
