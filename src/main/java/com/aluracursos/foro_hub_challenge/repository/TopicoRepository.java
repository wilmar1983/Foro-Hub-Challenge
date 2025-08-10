package com.aluracursos.foro_hub_challenge.repository;

import com.aluracursos.foro_hub_challenge.domain.Topico;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface TopicoRepository extends JpaRepository<Topico, Long> {
    Page<Topico> findByEstadoTrue(Pageable paginacion);
    List<Topico> findByEstadoTrue();

}

