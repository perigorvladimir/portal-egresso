package com.ufma.portalegressos.application.usecases.cargo;

import com.ufma.portalegressos.application.domain.Cargo;

import java.util.Optional;

public interface BuscarCargoPorId {
    Optional<Cargo> buscarPorId(Integer id);
}
