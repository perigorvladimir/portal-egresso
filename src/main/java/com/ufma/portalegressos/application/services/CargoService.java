package com.ufma.portalegressos.application.services;

import com.ufma.portalegressos.application.domain.Cargo;
import com.ufma.portalegressos.application.domain.Egresso;
import com.ufma.portalegressos.application.out.CargoJpaRepository;
import com.ufma.portalegressos.application.out.EgressoJpaRepository;
import com.ufma.portalegressos.application.usecases.cargo.CargoUC;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@AllArgsConstructor
@Service
public class CargoService implements CargoUC {
    private final CargoJpaRepository cargoJpaRepository;
    private final EgressoJpaRepository egressoJpaRepository;
    @Override
    public Cargo salvar(Request request) {
        Egresso egressoEncontrado = egressoJpaRepository.findById(request.getIdEgresso()).orElseThrow(EntityNotFoundException::new);

        Cargo cargo = Cargo.builder()
                .egresso(egressoEncontrado)
                .local(request.getLocal())
                .descricao(request.getDescricao())
                .anoInicio(request.getAnoInicio())
                .anoFim(request.getAnoFim())
                .build();
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
