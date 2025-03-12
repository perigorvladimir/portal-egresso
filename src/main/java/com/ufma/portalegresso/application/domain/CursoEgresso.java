package com.ufma.portalegresso.application.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ufma.portalegresso.application.domain.relacionamentos.CursoEgressoId;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Objects;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Table(schema = "portalegresso", name="curso_egresso")
public class CursoEgresso {
    @EmbeddedId
    private CursoEgressoId id = new CursoEgressoId();
    @ManyToOne
    @JoinColumn(name="id_egresso", nullable=false)
    @NotNull
    @MapsId("idEgresso")
    @JsonIgnore
    private Egresso egresso;
    @ManyToOne
    @JoinColumn(name="id_curso", nullable=false)
    @NotNull
    @MapsId("idCurso")
    @JsonIgnore
    private Curso curso;
    @Column(nullable = false)
    private Integer anoInicio;
    private Integer anoFim;

    //pro set funcionar direito
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CursoEgresso that = (CursoEgresso) o;
        return Objects.equals(curso, that.curso) &&
                Objects.equals(egresso, that.egresso);
    }

    @Override
    public int hashCode() {
        return Objects.hash(curso, egresso);
    }
}
