package com.aluracursos.foro_hub_challenge.dto;

import com.aluracursos.foro_hub_challenge.domain.Topico;

import java.time.LocalDateTime;

public record DatosListadoTopico(
        Long id,
        String titulo,
        String mensaje,
        LocalDateTime fechaCreacion,
        String curso
) {
    // Constructor que convierte un Topico a DatosListadoTopico
    public DatosListadoTopico(Topico topico) {
        this(topico.getId(),
                topico.getTitulo(),
                topico.getMensaje(),
                topico.getFechaCreacion(),
                topico.getCurso().getNombre());
    }
}

