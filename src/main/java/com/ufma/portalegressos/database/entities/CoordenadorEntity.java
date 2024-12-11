package com.ufma.portalegressos.database.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;

import java.util.Set;

@Entity
@DynamicInsert
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
    @Column(nullable = false)
    private String senha;
    @Column(columnDefinition = "varchar(255) default 'egresso'", nullable = false)
    private String tipo;
    @OneToMany(mappedBy = "coordenador")
    private Set<CursoEntity> cursos;
}
