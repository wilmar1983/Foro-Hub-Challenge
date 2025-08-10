package com.aluracursos.foro_hub_challenge.dto;

import jakarta.validation.constraints.NotBlank;


public record DatosActualizarTopico(

        @NotBlank
        String titulo,
        @NotBlank
        String mensaje
) {}
