package com.aluracursos.foro_hub_challenge.domain;

import com.aluracursos.foro_hub_challenge.dto.DatosRegistroUsuario;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.List;

@Entity(name = "Usuario")
@Table(name = "usuarios")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    private String nombre;
    @Setter
    private String email;
    @Setter
    private String contrasena;

    // Constructor personalizado para el DTO DatosRegistroUsuario
    public Usuario(DatosRegistroUsuario datos) {
        this.nombre = datos.nombre();
        this.email = datos.email();
        this.contrasena = datos.contrasena(); // Este valor debe venir ya cifrado con BCrypt
    }

    public Usuario(String nombre, String email, String contrasena) {
        this.nombre = nombre;
        this.email = email;
        this.contrasena = contrasena;
    }

    // Implementación de métodos de UserDetails para Spring Security

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(); // Sin roles por ahora
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public String getPassword() {
        return this.contrasena;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
