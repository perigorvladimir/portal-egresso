package com.ufma.portalegresso.application.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name="curso_egresso")
public class CursoEgresso {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idCursoEgresso;
    @NotNull
    @Column(nullable = false)
    private Integer anoInicio;
    private Integer anoFim;
    @ManyToOne
    @JoinColumn(name="id_egresso", nullable=false)
    @NotNull
    private Egresso egresso;
    @ManyToOne
    @JoinColumn(name="id_Curso", nullable=false)
    @NotNull
    private Curso curso;
}
