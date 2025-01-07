package com.ufma.portalegressos.application.services;

import com.ufma.portalegressos.application.domain.Depoimento;
import com.ufma.portalegressos.application.domain.Egresso;
import com.ufma.portalegressos.application.out.DepoimentoJpaRepository;
import com.ufma.portalegressos.application.out.EgressoJpaRepository;
import com.ufma.portalegressos.application.usecases.depoimento.DepoimentoUC;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class DepoimentoService implements DepoimentoUC {
    private final DepoimentoJpaRepository depoimentoJpaRepository;
    private final EgressoJpaRepository egressoJpaRepository;
    @Override
    public Depoimento salvarDepoimento(Request request) {
        Egresso egressoEncontrado = egressoJpaRepository.findById(request.getIdEgresso()).orElseThrow(EntityNotFoundException::new);
        Depoimento depoimento = Depoimento.builder()
                .data(LocalDate.now())
                .texto(request.getTexto())
                .egresso(egressoEncontrado)
                .build();
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

    @Override
    public List<Depoimento> buscarDepoimentosPorAno(Integer ano) {
        return depoimentoJpaRepository.findByDataYear(ano);
    }

    @Override
    public List<Depoimento> buscarDepoimentosRecentes() {
        LocalDate dataBuscar = LocalDate.now().minusDays(30);
        return depoimentoJpaRepository.findByDataAfter(dataBuscar);
    }
}
