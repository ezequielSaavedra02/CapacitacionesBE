package com.example.capacitacionesbe.repository;

import com.example.capacitacionesbe.model.Cursos;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CursosRepository extends JpaRepository<Cursos, Long>{
}
