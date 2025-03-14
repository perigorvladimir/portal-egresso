package com.ufma.portalegresso.application.usecases.egresso;

import com.ufma.portalegresso.application.domain.Egresso;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

public interface UpdateEgressoUC {
    Egresso updateEgresso(Integer id, Request request);

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    class Request{
        @NotBlank
        String nome;
        @NotBlank
        @Email
        String email;
        String descricao;
        String foto;
        String linkedin;
        String instagram;
        String curriculo;
    }
}
