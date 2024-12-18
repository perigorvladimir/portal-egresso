package com.ufma.portalegressos.application.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import static java.util.Collections.emptyList;

@Entity
@DynamicInsert
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="coordenador")
public class Coordenador implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idCoordenador;
    @NotNull
    @Column(nullable = false)
    private String nome;
    @NotNull
    @Column(nullable=false, unique = true)
    private String login;
    @NotNull
    @Column(nullable = false)
    private String senha;
    @OneToMany(mappedBy = "coordenador")
    private Set<Curso> cursos;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority("ROLE_ADMIN"));
    }

    @Override
    public String getPassword() {
        return senha;
    }

    @Override
    public String getUsername() {
        return login;
    }
}
