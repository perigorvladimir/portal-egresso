package com.ufma.portalegresso.application.usecases.cargo;

import com.ufma.portalegresso.application.domain.Cargo;

public interface BuscarCargoPorId {
    Cargo buscarPorId(Integer id);
}
