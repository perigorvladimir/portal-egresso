package com.ufma.portalegresso.application.usecases.egresso;

import com.ufma.portalegresso.application.domain.Egresso;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public interface SalvarEgressoUC {
    Egresso salvarEgresso(Request request);

    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Getter
    class Request{
        @NotBlank
        String nome;
        @NotBlank
        String email;
        String descricao;
        String foto;
        String linkedin;
        String instagram;
        String curriculo;
    }
}
