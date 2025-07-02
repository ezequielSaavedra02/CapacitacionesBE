package com.example.capacitacionesbe.controller;

import com.example.capacitacionesbe.model.Cursos; // Asegúrate de que tu entidad se llama 'Cursos'
import com.example.capacitacionesbe.service.CursoService; // <--- ¡Importamos tu servicio!
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/cursos")
@CrossOrigin(origins = "http://localhost:5173") // Permite solicitudes desde tu aplicación React (ajusta el puerto si es diferente)
public class CursosController {

    private final CursoService cursoService; // <--- Ahora inyectamos el servicio

    // El servicio inyecta los repositorios al controlador

    @Autowired
    public CursosController(CursoService cursoService) { // <--- Constructor con el servicio
        this.cursoService = cursoService;
    }

    @GetMapping
    public List<Cursos> getAllCursos() {
        return cursoService.findAllCursos(); // Delegamos al servicio
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cursos> getCursoById(@PathVariable Long id) {
        Optional<Cursos> curso = cursoService.findCursoById(id); // Delegamos al servicio
        return curso.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Cursos> createCurso(@RequestBody Cursos curso) {
        try {
            Cursos savedCurso = cursoService.saveCurso(curso); // Delegamos al servicio
            return ResponseEntity.status(HttpStatus.CREATED).body(savedCurso);
        } catch (IllegalArgumentException e) {
            // Capturamos la excepción lanzada por el servicio si el responsable no existe o no se proporciona
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cursos> updateCurso(@PathVariable Long id, @RequestBody Cursos cursoDetails) {
        try {
            Cursos updatedCurso = cursoService.updateCurso(id, cursoDetails); // Delegamos al servicio
            return ResponseEntity.ok(updatedCurso);
        } catch (IllegalArgumentException e) {
            // Capturamos la excepción si el responsable no se encuentra o no se proporciona en la actualización
            return ResponseEntity.badRequest().body(null);
        } catch (RuntimeException e) {
            // Capturamos la excepción si el curso no se encuentra
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCurso(@PathVariable Long id) {
        try {
            cursoService.deleteCurso(id); // Delegamos al servicio
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            // Capturamos la excepción si el curso no se encuentra
            return ResponseEntity.notFound().build();
        }
    }
}