package com.ufma.portalegressos.application.usecases.depoimento;

import com.ufma.portalegressos.application.domain.Depoimento;

import java.util.List;

public interface BuscarDepoimentosPorAnoUC {
    List<Depoimento> buscarDepoimentosPorAno(Integer ano);
}