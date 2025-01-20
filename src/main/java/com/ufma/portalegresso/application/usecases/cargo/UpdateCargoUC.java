package com.ufma.portalegresso.application.usecases.cargo;

import com.ufma.portalegresso.application.domain.Cargo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public interface UpdateCargoUC {
    Cargo updateCargo(Integer id, Request request);

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    class Request{
        String descricao;
        String local;
        Integer anoInicio;
        Integer anoFim;
    }
}
