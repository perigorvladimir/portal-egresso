package com.ufma.portalegresso.application.services;

import com.ufma.portalegresso.application.domain.Depoimento;
import com.ufma.portalegresso.application.domain.Egresso;
import com.ufma.portalegresso.application.out.DepoimentoJpaRepository;
import com.ufma.portalegresso.application.usecases.depoimento.DepoimentoUC;
import com.ufma.portalegresso.application.usecases.depoimento.SalvarDepoimentoUC;
import com.ufma.portalegresso.application.usecases.depoimento.UpdateDepoimentoUC;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@Service
public class DepoimentoService implements DepoimentoUC {
    private final DepoimentoJpaRepository depoimentoJpaRepository;
    private final EgressoService egressoService;
    @Override
    public Depoimento salvarDepoimento(SalvarDepoimentoUC.Request request) {
        Egresso egressoEncontrado = egressoService.buscarEgressoPorId(request.getIdEgresso());
        Depoimento depoimento = Depoimento.builder()
                .data(LocalDate.now())
                .texto(request.getTexto())
                .egresso(egressoEncontrado)
                .build();
        return depoimentoJpaRepository.save(depoimento);
    }

    @Override
    public Depoimento buscarDepoimentoPorId(Integer id) {
        if(id == null){
            throw new IllegalArgumentException("O id do depoimento nao pode ser nulo");
        }
        Depoimento depoimento = depoimentoJpaRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Depoimento nao encontrado com o id inserido"));
        return depoimento;
    }

    @Override
    public void deletarPorId(Integer id) {
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

    @Override
    public Depoimento updateDepoimento(Integer id, UpdateDepoimentoUC.Request request) {
        Depoimento depoimento = buscarDepoimentoPorId(id);
        depoimento.setTexto(request.getTexto());
        return depoimentoJpaRepository.saveAndFlush(depoimento);
    }


    @Override
    public List<Depoimento> buscarDepoimentoPorEgresso(Integer idEgresso) {
        return depoimentoJpaRepository.findByEgresso_IdEgresso(idEgresso);
    }
}
