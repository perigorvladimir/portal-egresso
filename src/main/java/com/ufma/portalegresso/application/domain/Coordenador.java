package com.ufma.portalegresso.application.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Entity
@DynamicInsert
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(schema = "portalegresso", name="coordenador")
public class Coordenador implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idCoordenador;
    @Column(nullable = false)
    private String nome;
    @Column(nullable=false, unique = true)
    private String login;
    @Column(nullable = false)
    private String senha;
    @Column(columnDefinition = "varchar(255) default 'ROLE_ADMIN'", nullable = false)
    private String role = "ROLE_ADMIN";
    @OneToMany(mappedBy = "coordenador")
    @JsonIgnoreProperties({"coordenador"})
    private Set<Curso> cursos;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"));
    }

    @Override
    public String getPassword() {
        return senha;
    }

    @Override
    public String getUsername() {
        return login;
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
