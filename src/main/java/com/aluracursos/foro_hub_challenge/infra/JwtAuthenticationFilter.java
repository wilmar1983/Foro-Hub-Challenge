package com.aluracursos.foro_hub_challenge.infra;

import com.aluracursos.foro_hub_challenge.domain.Usuario;
import com.aluracursos.foro_hub_challenge.repository.UsuarioRepository;
import com.aluracursos.foro_hub_challenge.service.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UsuarioRepository usuarioRepository;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, JwtService jwtService, UsuarioRepository usuarioRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.usuarioRepository = usuarioRepository;
        setFilterProcessesUrl("/login"); // importante: sobreescribe el endpoint
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            LoginRequest login = mapper.readValue(request.getInputStream(), LoginRequest.class);
            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(login.email(), login.contrasena())
            );
        } catch (IOException e) {
            throw new RuntimeException("Error al leer credenciales", e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {

        Usuario usuario = (Usuario) authResult.getPrincipal();
        String token = jwtService.generateToken(usuario.getEmail());

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        Map<String, String> body = new HashMap<>();
        body.put("token", token);

        new ObjectMapper().writeValue(response.getWriter(), body);
    }

    private record LoginRequest(String email, String contrasena) {
    }
}

