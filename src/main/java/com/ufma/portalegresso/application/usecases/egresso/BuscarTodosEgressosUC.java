package com.ufma.portalegresso.application.usecases.egresso;

import com.ufma.portalegresso.application.domain.Egresso;

import java.util.List;

public interface BuscarTodosEgressosUC {
    List<Egresso> buscarTodosEgressos();
}
