package com.ufma.portalegresso.application.usecases.cargo;

import com.ufma.portalegresso.application.domain.Cargo;
import com.ufma.portalegresso.application.domain.TipoAreaTrabalho;
import com.ufma.portalegresso.shared.validators.MaxAnoAtual;
import com.ufma.portalegresso.shared.validators.ValidEnum;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public interface SalvarCargoUC {
    Cargo salvar(Request request);

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    class Request{
        @NotBlank
        private String descricao;
        @NotBlank
        private String local;
        @NotNull
        @Min(1900)
        @MaxAnoAtual
        private Integer anoInicio;
        @Min(1900)
        @MaxAnoAtual
        private Integer anoFim;
        @NotNull
        private Integer idEgresso;
        @ValidEnum(enumClass = TipoAreaTrabalho.class)
        private String tipoAreaTrabalho;
    }
}
