package com.ufma.portalegressos.application.usecases.depoimento;

import com.ufma.portalegressos.application.domain.Depoimento;

import java.util.Optional;

public interface BuscarDepoimentoPorIdUc {
    Depoimento buscarDepoimentoPorId(Integer id);
}
