package com.ufma.portalegresso.application.services;

import com.ufma.portalegresso.application.domain.Depoimento;
import com.ufma.portalegresso.application.domain.Egresso;
import com.ufma.portalegresso.application.out.DepoimentoJpaRepository;
import com.ufma.portalegresso.application.out.EgressoJpaRepository;
import com.ufma.portalegresso.application.usecases.depoimento.SalvarDepoimentoUC;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class DepoimentoServiceTest {
    @Autowired
    private DepoimentoService service;
    @Autowired
    private DepoimentoJpaRepository depoimentoJpaRepository;
    private static Egresso egressoBase;
    @BeforeAll
    public static void setUp(@Autowired EgressoJpaRepository egressoJpaRepository) {
        Egresso egresso = Egresso.builder()
                .nome("egresso para teste")
                .email("egressoparateste@gmail.com")
                .descricao("descricao egresso para teste")
                .curriculo("curriculo egresso para teste")
                .instagram("egresso_teste")
                .linkedin("https://linkedin/egressoTeste")
                .foto("foto codificada")
                .build();
        egressoBase = egressoJpaRepository.save(egresso);
    }
    @AfterAll
    public static void cleanUp(@Autowired EgressoJpaRepository egressoJpaRepository) {
        egressoJpaRepository.deleteById(egressoBase.getIdEgresso());
    }

    @Test
    @Transactional
    public void deveSalvarDepoimentoPadrao() {
        SalvarDepoimentoUC.Request depoimento = SalvarDepoimentoUC.Request.builder()
                .texto("depoimento padrao")
                .idEgresso(egressoBase.getIdEgresso())
                .build();

        Depoimento depoimentoSalvo = service.salvarDepoimento(depoimento);

        assertNotNull(depoimentoSalvo);
        assertNotNull(depoimentoSalvo.getIdDepoimento());
        assertEquals(depoimento.getTexto(), depoimentoSalvo.getTexto());
        assertEquals(depoimento.getIdEgresso(), depoimentoSalvo.getEgresso().getIdEgresso());
        assertEquals(LocalDate.now().getDayOfMonth(), depoimentoSalvo.getData().getDayOfMonth());
        assertEquals(LocalDate.now().getMonth(), depoimentoSalvo.getData().getMonth());
    }

    @Test
    public void deveGerarErroAoTentarSalvarDepoimentoSemEgresso() {
        SalvarDepoimentoUC.Request depoimento = SalvarDepoimentoUC.Request.builder()
                .texto("depoimento padrao")
                .build();
        assertThrows(InvalidDataAccessApiUsageException.class, () -> service.salvarDepoimento(depoimento));
    }

    @Test
    @Transactional
    public void deveBuscarDepoimentoPorIdPadrao() {
        Depoimento depoimentoCriado = service.salvarDepoimento(SalvarDepoimentoUC.Request.builder()
                .idEgresso(egressoBase.getIdEgresso())
                .texto("depoimento padrao")
                .build());

        Depoimento depoimentoEncontrado = service.buscarDepoimentoPorId(depoimentoCriado.getIdDepoimento());

        assertEquals(depoimentoCriado.getIdDepoimento(), depoimentoEncontrado.getIdDepoimento());
        assertEquals(depoimentoCriado.getTexto(), depoimentoEncontrado.getTexto());
        assertEquals(depoimentoCriado.getEgresso().getIdEgresso(), depoimentoEncontrado.getEgresso().getIdEgresso());
    }

    @Test
    public void deveGerarErroAoTentarBuscarDepoimentoPorIdInexistente() {
        assertThrows(EntityNotFoundException.class, () -> service.buscarDepoimentoPorId(100));
    }

    @Test
    public void deveDeletarDepoimentoPadrao() {
        Depoimento depoimentoCriado = service.salvarDepoimento(SalvarDepoimentoUC.Request.builder()
                .idEgresso(egressoBase.getIdEgresso())
                .texto("depoimento padrao")
                .build());

        service.deletarPorId(depoimentoCriado.getIdDepoimento());

        Optional<Depoimento> depoimentoEncontrado = depoimentoJpaRepository.findById(depoimentoCriado.getIdDepoimento());
        assertTrue(depoimentoEncontrado.isEmpty());
    }

    @Test
    @Transactional
    public void deveBuscarDepoimentosPorAnoPadrao(){
        Depoimento depoimentoCriado = service.salvarDepoimento(SalvarDepoimentoUC.Request.builder()
                .idEgresso(egressoBase.getIdEgresso())
                .texto("depoimento padrao")
                .build());
        Depoimento depoimentoCriado2 = service.salvarDepoimento(SalvarDepoimentoUC.Request.builder()
                .idEgresso(egressoBase.getIdEgresso())
                .texto("depoimento padrao 2")
                .build());
        List<Depoimento> depoimentosEncontrados = service.buscarDepoimentosPorAno(depoimentoCriado.getData().getYear());
        assertEquals(2, depoimentosEncontrados.size());
    }

    @Test
    @Transactional
    public void deveBuscarDepoimentosRecentesFluxoPadrao(){
        service.salvarDepoimento(SalvarDepoimentoUC.Request.builder()
                .idEgresso(egressoBase.getIdEgresso())
                .texto("depoimento padrao")
                .build());
        service.salvarDepoimento(SalvarDepoimentoUC.Request.builder()
                .idEgresso(egressoBase.getIdEgresso())
                .texto("depoimento padrao 2")
                .build());
        service.salvarDepoimento(SalvarDepoimentoUC.Request.builder()
                .idEgresso(egressoBase.getIdEgresso())
                .texto("depoimento padrao 3")
                .build());
        depoimentoJpaRepository.save(Depoimento.builder()
                .data(LocalDate.now().minusDays(40))
                .egresso(egressoBase)
                .texto("depoimento antigo")
                .build());

        List<Depoimento> depoimentosEncontrados = service.buscarDepoimentosRecentes();

        assertEquals(3, depoimentosEncontrados.size());
    }
}
