package com.ufma.portalegresso.application.usecases.egresso;

import com.ufma.portalegresso.application.domain.Egresso;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
        @Email
        String email;
        String descricao;
        String foto;
        String linkedin;
        String instagram;
        String curriculo;
        @NotNull
        Integer idCurso;
        @NotNull
        Integer anoInicioCurso;
        @NotNull
        Integer anoFimCurso;
    }
}
