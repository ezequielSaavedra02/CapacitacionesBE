package com.example.capacitacionesbe.repository;

import com.example.capacitacionesbe.model.Participaciones;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ParticipacionesRepository extends JpaRepository<Participaciones, Long> {
    List<Participaciones> findAll();
}
