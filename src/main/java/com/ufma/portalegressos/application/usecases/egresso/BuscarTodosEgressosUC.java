package com.ufma.portalegressos.application.usecases.egresso;

import com.ufma.portalegressos.application.domain.Egresso;

import java.util.List;

public interface BuscarTodosEgressosUC {
    List<Egresso> buscarTodosEgressos();
}
