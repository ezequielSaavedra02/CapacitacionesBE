package com.example.capacitacionesbe.controller;

import com.example.capacitacionesbe.model.Participaciones; // Asegúrate que tu entidad se llama 'Participaciones'
import com.example.capacitacionesbe.service.ParticipacionService; // <--- ¡Importamos tu servicio!
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/participaciones")
@CrossOrigin(origins = "http://localhost:5173") // Permite solicitudes desde tu aplicación React (ajusta el puerto si es diferente)
public class ParticipacionesController {

    private final ParticipacionService participacionService; // <--- Ahora inyectamos el servicio

    // el servicio se encarga de las interacciones con la base de datos.

    @Autowired
    public ParticipacionesController(ParticipacionService participacionService) { // <--- Constructor con el servicio
        this.participacionService = participacionService;
    }

    @GetMapping
    public List<Participaciones> getAllParticipaciones() {
        return participacionService.findAllParticipaciones(); // Delegamos al servicio
    }

    @GetMapping("/{id}")
    public ResponseEntity<Participaciones> getParticipacionById(@PathVariable Long id) {
        Optional<Participaciones> participacion = participacionService.findParticipacionById(id); // Delegamos al servicio
        return participacion.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Participaciones> createParticipacion(@RequestBody Participaciones participacion) {
        try {
            Participaciones savedParticipacion = participacionService.saveParticipacion(participacion); // Delegamos al servicio
            return ResponseEntity.status(HttpStatus.CREATED).body(savedParticipacion);
        } catch (IllegalArgumentException e) {
            // Capturamos excepciones de validación o no encontrado desde el servicio
            return ResponseEntity.badRequest().body(null); // O puedes devolver un mensaje de error más específico
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Participaciones> updateParticipacion(@PathVariable Long id, @RequestBody Participaciones participacionDetails) {
        try {
            Participaciones updatedParticipacion = participacionService.updateParticipacion(id, participacionDetails); // Delegamos al servicio
            return ResponseEntity.ok(updatedParticipacion);
        } catch (IllegalArgumentException e) {
            // Capturamos excepciones de validación (ej. curso/actividad no existe, ambos/ninguno, etc.)
            return ResponseEntity.badRequest().body(null);
        } catch (RuntimeException e) {
            // Capturamos excepciones de "no encontrado" del servicio
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteParticipacion(@PathVariable Long id) {
        try {
            participacionService.deleteParticipacion(id); // Delegamos al servicio
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            // Capturamos excepciones de "no encontrado" del servicio
            return ResponseEntity.notFound().build();
        }
    }
}
