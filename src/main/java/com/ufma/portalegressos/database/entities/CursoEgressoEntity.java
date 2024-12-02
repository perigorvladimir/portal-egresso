package com.ufma.portalegressos.database.entities;

import jakarta.persistence.*;
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
public class CursoEgressoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idCursoEgresso;
    @ManyToOne
    @JoinColumn(name="id_egresso", nullable=false)
    private EgressoEntity egresso;
    @ManyToOne
    @JoinColumn(name="id_Curso", nullable=false)
    private CursoEntity curso;
}
