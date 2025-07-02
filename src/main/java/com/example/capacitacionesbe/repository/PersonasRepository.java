package com.example.capacitacionesbe.repository;

import com.example.capacitacionesbe.model.Personas;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PersonasRepository extends JpaRepository<Personas, Long> {

    void deleteById(Long id);

    boolean existsById(Long id);

    List<Personas> findAll();
}
