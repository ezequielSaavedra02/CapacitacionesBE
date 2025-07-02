package com.example.capacitacionesbe.controller;

import com.example.capacitacionesbe.model.Personas; // Asegúrate de que tu entidad se llame 'Personas'
import com.example.capacitacionesbe.service.PersonaService; // <--- Importa tu servicio de Personas
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/personas") // Ruta base para todas las operaciones de Persona
@CrossOrigin(origins = "http://localhost:5173") // Permite solicitudes desde tu aplicación React (ajusta el puerto si es diferente)
public class PersonasController {

    private final PersonaService personasService; // <--- Ahora inyectamos el servicio

    // el servicio se encarga de las interacciones con la base de datos.

    @Autowired // Spring inyectará automáticamente una instancia de PersonasService
    public PersonasController(PersonaService personasService) { // <--- Constructor para el servicio
        this.personasService = personasService;
    }

    // Obtener todas las personas
    @GetMapping
    public List<Personas> getAllPersonas() {
        return personasService.findAllPersonas(); // Llama al método del servicio
    }

    // Obtener una persona por ID
    @GetMapping("/{id}")
    public ResponseEntity<Personas> getPersonaById(@PathVariable Long id) {
        Optional<Personas> persona = personasService.findPersonaById(id); // Llama al servicio
        return persona.map(ResponseEntity::ok) // Si encuentra la persona, devuelve 200 OK con la persona
                .orElseGet(() -> ResponseEntity.notFound().build()); // Si no, devuelve 404 Not Found
    }

    // Crear una nueva persona
    @PostMapping
    public ResponseEntity<Personas> createPersona(@RequestBody Personas persona) {
        try {
            Personas savedPersona = personasService.savePersona(persona); // Llama al servicio
            return ResponseEntity.status(HttpStatus.CREATED).body(savedPersona); // Devuelve 201 Created
        } catch (IllegalArgumentException e) {
            // Manejo de errores de validación desde el servicio
            return ResponseEntity.badRequest().body(null);
        }
    }

    // Actualizar una persona existente
    @PutMapping("/{id}")
    public ResponseEntity<Personas> updatePersona(@PathVariable Long id, @RequestBody Personas personaDetails) {
        try {
            Personas updatedPersona = personasService.updatePersona(id, personaDetails); // Llama al servicio
            return ResponseEntity.ok(updatedPersona);
        } catch (IllegalArgumentException e) {
            // Manejo de errores de validación si el servicio lanza IllegalArgumentException
            return ResponseEntity.badRequest().body(null);
        } catch (RuntimeException e) { // Captura la excepción si no se encuentra la persona
            return ResponseEntity.notFound().build();
        }
    }

    // Eliminar una persona
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePersona(@PathVariable Long id) {
        try {
            personasService.deletePersona(id); // Llama al servicio
            return ResponseEntity.noContent().build(); // Devuelve 204 No Content
        } catch (RuntimeException e) { // Captura la excepción si no se encuentra la persona
            return ResponseEntity.notFound().build();
        }
    }
}
