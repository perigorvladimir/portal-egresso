package com.ufma.portalegresso.application.usecases.curso;

import com.ufma.portalegresso.application.domain.Curso;
import com.ufma.portalegresso.application.domain.TipoNivel;
import com.ufma.portalegresso.shared.validators.ValidEnum;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public interface SalvarCursoUC {
    Curso salvarCurso(Request request);

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    class Request {
        @NotBlank
        private String nome;
        @ValidEnum(enumClass = TipoNivel.class)
        private String tipoNivel;
        private Integer idCoordenador;
    }
}
