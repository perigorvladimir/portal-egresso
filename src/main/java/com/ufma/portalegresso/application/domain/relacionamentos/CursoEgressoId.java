package com.ufma.portalegresso.application.domain.relacionamentos;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Embeddable
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class CursoEgressoId implements Serializable {
    @Column(name = "id_egresso")
    private Integer idEgresso;
    @Column(name = "id_curso")
    private Integer idCurso;
}

