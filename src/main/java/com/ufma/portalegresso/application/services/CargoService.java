package com.ufma.portalegresso.application.services;

import com.ufma.portalegresso.application.domain.Cargo;
import com.ufma.portalegresso.application.domain.Egresso;
import com.ufma.portalegresso.application.domain.TipoAreaTrabalho;
import com.ufma.portalegresso.application.out.CargoJpaRepository;
import com.ufma.portalegresso.application.usecases.cargo.CargoUC;
import com.ufma.portalegresso.application.usecases.cargo.SalvarCargoUC;
import com.ufma.portalegresso.application.usecases.cargo.UpdateCargoUC;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class CargoService implements CargoUC {
    private final CargoJpaRepository cargoJpaRepository;
    private final EgressoService egressoService;
    @Override
    public Cargo salvar(SalvarCargoUC.Request request) {
        validarAnoInicioEAnoFim(request.getAnoInicio(), request.getAnoFim());

        Egresso egressoEncontrado = egressoService.buscarEgressoPorId(request.getIdEgresso());

        Cargo cargo = Cargo.builder()
                .egresso(egressoEncontrado)
                .local(request.getLocal())
                .descricao(request.getDescricao())
                .anoInicio(request.getAnoInicio())
                .anoFim(request.getAnoFim())
                .tipoAreaTrabalho(TipoAreaTrabalho.buscarPorNome(request.getTipoAreaTrabalho()))
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
    public List<Cargo> buscarTodosCargos() {
        return cargoJpaRepository.findAll();
    }

    @Override
    public void deletarPorId(Integer id) {
        cargoJpaRepository.deleteById(id);
    }

    @Override
    public Cargo updateCargo(Integer id, UpdateCargoUC.Request request) {
        validarAnoInicioEAnoFim(request.getAnoInicio(), request.getAnoFim());
        Cargo cargo = buscarPorId(id);
        cargo.setLocal(request.getLocal());
        cargo.setDescricao(request.getDescricao());
        cargo.setAnoInicio(request.getAnoInicio());
        cargo.setAnoFim(request.getAnoFim());
        cargo.setTipoAreaTrabalho(TipoAreaTrabalho.buscarPorNome(request.getTipoAreaTrabalho()));
        return cargoJpaRepository.save(cargo);
    }

    private void validarAnoInicioEAnoFim(Integer anoInicio, Integer anoFim){
        if(anoFim != null && anoInicio != null && anoFim < anoInicio){
            throw new IllegalArgumentException("O ano de fim deve ser maior que o de inicio");
        }
    }
}
