package com.aluracursos.foro_hub_challenge.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record DatosRegistroUsuario(
        @NotBlank(message = "El nombre es obligatorio")
        String nombre,

        @Email(message = "Debe ser un email válido")
        @NotBlank(message = "El email es obligatorio")
        String email,

        @NotBlank(message = "La contraseña es obligatoria")
        String contrasena
) {}
