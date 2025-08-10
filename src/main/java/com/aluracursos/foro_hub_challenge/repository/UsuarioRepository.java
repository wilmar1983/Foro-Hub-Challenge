package com.aluracursos.foro_hub_challenge.repository;

import com.aluracursos.foro_hub_challenge.domain.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);
}
