package com.ufma.portalegresso.application.domain;


import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
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
    @Column(nullable = false)
    private String nome;
    @Column(nullable = false)
    private String email;
    private String descricao;
    private String foto;
    private String linkedin;
    private String instagram;
    private String curriculo;
    @OneToMany(mappedBy="egresso", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties({"egresso"})
    private List<Cargo> cargos;
    @OneToMany(mappedBy="egresso", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<CursoEgresso> cursoEgressos;
    @OneToMany(mappedBy="egresso", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties({"egresso"})
    private Set<Depoimento> depoimentos;

    public Set<Curso> getCursos() {
        if(cursoEgressos==null){
            return null;
        }
        Set<Curso> set = new HashSet<>();
        for(CursoEgresso cursoMatriculado: this.cursoEgressos){
            set.add(cursoMatriculado.getCurso());
        }
        return set;
    }
}
