package com.ufma.portalegresso.application.domain.relacionamentos;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@AllArgsConstructor
@Builder
@Getter
@Setter
@NoArgsConstructor
public class CursoEgressoId implements Serializable {
    @Column(name = "id_egresso")
    private Integer idEgresso;
    @Column(name = "id_curso")
    private Integer idCurso;
}

