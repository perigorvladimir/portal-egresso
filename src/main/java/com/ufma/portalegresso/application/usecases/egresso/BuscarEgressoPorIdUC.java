package com.ufma.portalegresso.application.usecases.egresso;

import com.ufma.portalegresso.application.domain.Egresso;

public interface BuscarEgressoPorIdUC {
    Egresso buscarEgressoPorId(Integer id);
}
