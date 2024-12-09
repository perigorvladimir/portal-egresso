package com.ufma.portalegressos.database.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Set;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="coordenador")
public class CoordenadorEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idCoordenador;
    @NotNull
    private String login;
    @NotNull
    private String senha;
    @NotNull
    @Column(columnDefinition = "varchar(255) default 'egresso'", nullable = false)
    private String tipo = "egresso";
    @OneToMany(mappedBy = "coordenador")
    private Set<CursoEntity> cursos;
}
