package com.ufma.portalegressos.database.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

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
    private Egresso egresso;
    @ManyToOne
    @JoinColumn(name="id_Curso", nullable=false)
    private Curso curso;
}
