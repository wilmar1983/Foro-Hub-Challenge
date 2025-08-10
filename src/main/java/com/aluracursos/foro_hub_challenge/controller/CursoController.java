package com.aluracursos.foro_hub_challenge.controller;

import com.aluracursos.foro_hub_challenge.domain.Curso;
import com.aluracursos.foro_hub_challenge.dto.DatosRegistroCurso;
import com.aluracursos.foro_hub_challenge.repository.CursoRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cursos")
public class CursoController {

    @Autowired
    private CursoRepository cursoRepository;

    @PostMapping
    public ResponseEntity<Void> registrar(@RequestBody @Valid DatosRegistroCurso datos) {
        var curso = new Curso(null, datos.nombre(), datos.categoria());
        cursoRepository.save(curso);
        return ResponseEntity.ok().build();
    }
}

