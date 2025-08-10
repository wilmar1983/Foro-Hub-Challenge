package com.aluracursos.foro_hub_challenge.controller;

import com.aluracursos.foro_hub_challenge.domain.Usuario;
import com.aluracursos.foro_hub_challenge.dto.DatosAutenticacionUsuario;
import com.aluracursos.foro_hub_challenge.service.JwtService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/login")
public class AutenticacionController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @PostMapping
    public ResponseEntity<String> autenticarUsuario(@RequestBody @Valid DatosAutenticacionUsuario datosAutenticacionUsuario) {
        try {
            Authentication authToken = new UsernamePasswordAuthenticationToken(
                    datosAutenticacionUsuario.email(),
                    datosAutenticacionUsuario.contrasena()
            );

            var usuarioAutenticado = authenticationManager.authenticate(authToken);

            // 🔐 Generar el token usando el Usuario autenticado
            var usuario = (Usuario) usuarioAutenticado.getPrincipal();
            var jwt = jwtService.generateToken(usuario);

            return ResponseEntity.ok(jwt);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(403).body("Credenciales inválidas");
        }
    }
}
