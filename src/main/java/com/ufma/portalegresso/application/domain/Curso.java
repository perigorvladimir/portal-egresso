package com.ufma.portalegresso.application.domain;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name="curso")
public class Curso {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idCurso;
    @NotNull
    private String nome;
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoNivel tipoNivel;
    @OneToMany(mappedBy = "curso")
    private Set<CursoEgresso> cursoEgressos;
    @ManyToOne
    @JoinColumn(name="id_coordenador")
    private Coordenador coordenador;
}
