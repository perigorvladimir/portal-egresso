package com.ufma.portalegresso.application.usecases.cargo;

import com.ufma.portalegresso.application.domain.Cargo;
import com.ufma.portalegresso.shared.validators.MaxAnoAtual;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

public interface UpdateCargoUC {
    Cargo updateCargo(Integer id, Request request);

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    class Request{
        String descricao;
        String local;
        @Min(1900)
        @MaxAnoAtual
        Integer anoInicio;
        @Min(1900)
        @MaxAnoAtual
        Integer anoFim;
    }
}
