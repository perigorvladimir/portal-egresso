package com.ufma.portalegresso.application.usecases.depoimento;

import com.ufma.portalegresso.application.domain.Depoimento;

public interface BuscarDepoimentoPorIdUC {
    Depoimento buscarDepoimentoPorId(Integer id);
}
