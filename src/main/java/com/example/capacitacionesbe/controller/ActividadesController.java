package com.example.capacitacionesbe.controller;

import com.example.capacitacionesbe.model.Actividades; // Asegúrate de que tu entidad se llama 'Actividades'
import com.example.capacitacionesbe.service.ActividadService; // <--- ¡Importamos tu servicio!
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/actividades")
@CrossOrigin(origins = "http://localhost:5173") // Permite solicitudes desde tu aplicación React (ajusta el puerto si es diferente)
public class ActividadesController {

    private final ActividadService actividadService; // <--- Ahora inyectamos el servicio

    // Ya no inyectamos directamente los repositorios aquí;
    // el servicio se encarga de las interacciones con la base de datos.

    @Autowired
    public ActividadesController(ActividadService actividadService) { // <--- Constructor con el servicio
        this.actividadService = actividadService;
    }

    @GetMapping
    public List<Actividades> getAllActividades() {
        return actividadService.findAllActividades(); // Delegamos al servicio
    }

    @GetMapping("/{id}")
    public ResponseEntity<Actividades> getActividadById(@PathVariable Long id) {
        Optional<Actividades> actividad = actividadService.findActividadById(id); // Delegamos al servicio
        return actividad.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Actividades> createActividad(@RequestBody Actividades actividad) {
        try {
            Actividades savedActividad = actividadService.saveActividad(actividad); // Delegamos al servicio
            return ResponseEntity.status(HttpStatus.CREATED).body(savedActividad);
        } catch (IllegalArgumentException e) {
            // Capturamos la excepción lanzada por el servicio (ej. responsable no existe o no proporcionado)
            return ResponseEntity.badRequest().body(null); // O puedes devolver un mensaje de error más específico
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Actividades> updateActividad(@PathVariable Long id, @RequestBody Actividades actividadDetails) {
        try {
            Actividades updatedActividad = actividadService.updateActividad(id, actividadDetails); // Delegamos al servicio
            return ResponseEntity.ok(updatedActividad);
        } catch (IllegalArgumentException e) {
            // Capturamos la excepción si el responsable no se encuentra o no se proporciona en la actualización
            return ResponseEntity.badRequest().body(null);
        } catch (RuntimeException e) {
            // Capturamos la excepción si la actividad no se encuentra
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteActividad(@PathVariable Long id) {
        try {
            actividadService.deleteActividad(id); // Delegamos al servicio
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            // Capturamos la excepción si la actividad no se encuentra
            return ResponseEntity.notFound().build();
        }
    }
}