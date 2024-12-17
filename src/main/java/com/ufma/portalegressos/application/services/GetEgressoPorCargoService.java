package com.ufma.portalegressos.application.services;

import com.ufma.portalegressos.application.usecases.GetEgressosPorCargoUc;
import org.springframework.stereotype.Service;

@Service
public class GetEgressoPorCargoService implements GetEgressosPorCargoUc {
    private final EgressoRepository egressoRepository;

    public List<Egresso> execute(TipoCargo tipoCargo){
        return egressoRepository.getEgressoPorCargo(TipoCargo tipoCargo);
    }
}
