package com.ufma.portalegressos.database.entities;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name="curso")
public class Curso {
    @Id
    private Integer idCurso;
    @NotNull
    private String nome;
    @NotNull
    private String nivel;
    @OneToMany(mappedBy = "curso")
    private Set<CursoEgresso> cursoEgressos;
}
