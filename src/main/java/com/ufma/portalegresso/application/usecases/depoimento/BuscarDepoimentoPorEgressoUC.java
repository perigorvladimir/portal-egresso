package com.ufma.portalegresso.application.usecases.depoimento;

import com.ufma.portalegresso.application.domain.Depoimento;

import java.util.List;

public interface BuscarDepoimentoPorEgressoUC {
    List<Depoimento> buscarDepoimentoPorEgresso(Integer idEgresso); //TODO fazer um buscar por filtro
}
