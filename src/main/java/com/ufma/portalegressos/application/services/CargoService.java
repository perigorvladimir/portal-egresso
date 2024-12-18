package com.ufma.portalegressos.application.services;

import com.ufma.portalegressos.application.domain.Cargo;
import com.ufma.portalegressos.application.repositories.CargoJpaRepository;
import com.ufma.portalegressos.application.usecases.cargo.CargoUC;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@AllArgsConstructor
@Service
public class CargoService implements CargoUC {
    private final CargoJpaRepository cargoJpaRepository;
    @Override
    public Cargo salvar(Cargo cargo) {
        return cargoJpaRepository.save(cargo);
    }

    @Override
    public Optional<Cargo> buscarPorId(Integer id) {
        return cargoJpaRepository.findById(id);
    }

    @Override
    public void deletar(Integer id) {
        cargoJpaRepository.deleteById(id);
    }
}
