package com.aluracursos.foro_hub_challenge.service;

import com.aluracursos.foro_hub_challenge.dto.DatosRegistroUsuario;
import com.aluracursos.foro_hub_challenge.domain.Usuario;
import com.aluracursos.foro_hub_challenge.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Usuario registrarUsuario(DatosRegistroUsuario datos) {
        String contrasenaEncriptada = passwordEncoder.encode(datos.contrasena());
        Usuario nuevoUsuario = new Usuario(datos.nombre(), datos.email(), contrasenaEncriptada);
        return usuarioRepository.save(nuevoUsuario);
    }
}