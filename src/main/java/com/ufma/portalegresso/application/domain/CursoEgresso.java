package com.ufma.portalegresso.application.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name="curso_egresso")
public class CursoEgresso {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idCursoEgresso;
    @ManyToOne
    @JoinColumn(name="id_egresso", nullable=false)
    @NotNull
    private Egresso egresso;
    @ManyToOne
    @JoinColumn(name="id_Curso", nullable=false)
    @NotNull
    private Curso curso;
}
