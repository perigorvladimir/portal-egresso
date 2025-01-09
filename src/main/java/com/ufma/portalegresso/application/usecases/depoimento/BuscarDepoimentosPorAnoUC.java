package com.ufma.portalegresso.application.usecases.depoimento;

import com.ufma.portalegresso.application.domain.Depoimento;

import java.util.List;

public interface BuscarDepoimentosPorAnoUC {
    List<Depoimento> buscarDepoimentosPorAno(Integer ano);
}
