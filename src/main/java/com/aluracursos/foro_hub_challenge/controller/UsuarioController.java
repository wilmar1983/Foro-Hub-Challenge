package com.aluracursos.foro_hub_challenge.controller;


import com.aluracursos.foro_hub_challenge.dto.DatosRegistroUsuario;
import com.aluracursos.foro_hub_challenge.domain.Usuario;
import com.aluracursos.foro_hub_challenge.dto.DatosRespuestaUsuario;
import com.aluracursos.foro_hub_challenge.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<DatosRespuestaUsuario> registrar(@RequestBody @Valid DatosRegistroUsuario datos) {
        Usuario usuario = usuarioService.registrarUsuario(datos);
        DatosRespuestaUsuario respuesta = new DatosRespuestaUsuario(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getEmail()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
    }
}