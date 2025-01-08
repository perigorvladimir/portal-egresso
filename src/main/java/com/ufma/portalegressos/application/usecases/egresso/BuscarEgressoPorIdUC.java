package com.ufma.portalegressos.application.usecases.egresso;

import com.ufma.portalegressos.application.domain.Egresso;

import java.util.Optional;

public interface BuscarEgressoPorIdUC {
    Egresso buscarEgressoPorId(Integer id);
}
