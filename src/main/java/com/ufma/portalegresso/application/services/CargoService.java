package com.ufma.portalegresso.application.services;

import com.ufma.portalegresso.application.domain.Cargo;
import com.ufma.portalegresso.application.domain.Egresso;
import com.ufma.portalegresso.application.out.CargoJpaRepository;
import com.ufma.portalegresso.application.out.EgressoJpaRepository;
import com.ufma.portalegresso.application.usecases.cargo.CargoUC;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class CargoService implements CargoUC {
    private final CargoJpaRepository cargoJpaRepository;
    private final EgressoJpaRepository egressoJpaRepository;
    @Override
    public Cargo salvar(Request request) {
        if(request.getIdEgresso()==null){
            throw new IllegalArgumentException("Campo Id Egresso nao pode ser nulo");
        }

        if(request.getAnoFim() < request.getAnoInicio()){
            throw new IllegalArgumentException("O ano de fim deve ser maior que o de inicio");
        }

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
    public Cargo buscarPorId(Integer id) {
        Optional<Cargo> cargo = cargoJpaRepository.findById(id);
        if(cargo.isEmpty()){
            throw new EntityNotFoundException("Cargo não encontrado com o id inserido");
        }
        return cargo.get();
    }

    @Override
    public void deletar(Integer id) {
        cargoJpaRepository.deleteById(id);
    }

    @Override
    public List<Cargo> buscarTodosCargos() {
        return cargoJpaRepository.findAll();
    }
}
