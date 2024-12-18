package com.ufma.portalegressos.application.services;

import com.ufma.portalegressos.application.domain.Depoimento;
import com.ufma.portalegressos.application.repositories.DepoimentoJpaRepository;
import com.ufma.portalegressos.application.usecases.depoimento.DepoimentoUC;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@AllArgsConstructor
@Service
public class DepoimentoService implements DepoimentoUC {
    private final DepoimentoJpaRepository depoimentoJpaRepository;

    @Override
    public Depoimento salvarDepoimento(Depoimento depoimento) {
        return depoimentoJpaRepository.save(depoimento);
    }

    @Override
    public Optional<Depoimento> buscarDepoimentoPorId(Integer id) {
        return depoimentoJpaRepository.findById(id);
    }

    @Override
    public void deletar(Integer id) {
        depoimentoJpaRepository.deleteById(id);
    }
}
