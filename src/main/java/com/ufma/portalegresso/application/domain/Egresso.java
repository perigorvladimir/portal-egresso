package com.ufma.portalegresso.application.domain;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name="egresso")
public class Egresso {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idEgresso;
    @NotNull
    private String nome;
    @NotNull
    private String email;
    private String descricao;
    private String foto;
    private String linkedin;
    private String instagram;
    private String curriculo;
    @OneToMany(mappedBy="egresso")
    private List<Cargo> cargos;
    @OneToMany(mappedBy="egresso", cascade = CascadeType.ALL)
    private Set<CursoEgresso> cursoEgressos;

    public void addCurso(Curso curso, Integer anoInicio, Integer anoFim) {
        CursoEgresso cursoEgresso = CursoEgresso.builder()
                .curso(curso)
                .egresso(this)
                .anoInicio(anoInicio)
                .anoFim(anoFim)
                .build();
        cursoEgressos.add(cursoEgresso);
    }
}
