package com.aluracursos.foro_hub_challenge.repository;

import com.aluracursos.foro_hub_challenge.domain.Curso;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CursoRepository extends JpaRepository<Curso, Long> {

    Curso findByNombre(String nombre);
}
