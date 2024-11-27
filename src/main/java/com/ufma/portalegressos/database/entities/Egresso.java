package com.ufma.portalegressos.database.entities;


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
    private String Instagram;
    private String curriculo;
    @OneToMany(mappedBy="egresso")
    private List<Cargo> cargos;
    @OneToMany(mappedBy="egresso")
    private Set<CursoEgresso> cursoEgressos;
}
