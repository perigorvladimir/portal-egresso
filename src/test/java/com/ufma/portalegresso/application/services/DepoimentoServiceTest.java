package com.ufma.portalegresso.application.services;

import com.ufma.portalegresso.application.domain.Depoimento;
import com.ufma.portalegresso.application.domain.Egresso;
import com.ufma.portalegresso.application.out.DepoimentoJpaRepository;
import com.ufma.portalegresso.application.out.EgressoJpaRepository;
import com.ufma.portalegresso.application.usecases.depoimento.SalvarDepoimentoUC;
import com.ufma.portalegresso.application.usecases.depoimento.UpdateDepoimentoUC;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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
    private EntityManager entityManager;
    @Autowired
    private DepoimentoService service;
    @Autowired
    private DepoimentoJpaRepository depoimentoJpaRepository;
    private static Egresso egressoBase;
    private static Egresso egressoBase2;
    @Autowired
    private DepoimentoService depoimentoService;

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
        Egresso egresso2 = Egresso.builder()
                .nome("egresso 2 para teste")
                .email("egressodois@yahoo.com")
                .descricao("EGRESSO 2")
                .build();
        egressoBase = egressoJpaRepository.save(egresso);
        egressoBase2 = egressoJpaRepository.save(egresso2);
    }
    @AfterAll
    public static void cleanUp(@Autowired EgressoService egressoService) {
        egressoService.deletarEgressoPorId(egressoBase.getIdEgresso());
        egressoService.deletarEgressoPorId(egressoBase2.getIdEgresso());
    }

    @Test
    @Transactional
    public void deveSalvarDepoimentoFluxoPadrao() {
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
    public void deveGerarErroAoSalvarDepoimentoIdEgressoNull() {
        SalvarDepoimentoUC.Request depoimento = SalvarDepoimentoUC.Request.builder()
                .texto("depoimento padrao")
                .idEgresso(null)
                .build();
        assertThrows(IllegalArgumentException.class, () -> service.salvarDepoimento(depoimento));
    }
    @Test
    public void deveGerarErroAoSalvarDepoimentoIdEgressoInexistente() {
        SalvarDepoimentoUC.Request depoimento = SalvarDepoimentoUC.Request.builder()
                .texto("depoimento padrao")
                .idEgresso(100)
                .build();
        assertThrows(EntityNotFoundException.class, () -> service.salvarDepoimento(depoimento));
    }
    @Test
    @Transactional
    public void deveBuscarDepoimentoPorIdPadrao() {
        Depoimento depoimentoCriado = criarDepoimentoTestes();

        Depoimento depoimentoEncontrado = service.buscarDepoimentoPorId(depoimentoCriado.getIdDepoimento());

        assertEquals(depoimentoCriado.getIdDepoimento(), depoimentoEncontrado.getIdDepoimento());
        assertEquals(depoimentoCriado.getTexto(), depoimentoEncontrado.getTexto());
        assertEquals(depoimentoCriado.getEgresso().getIdEgresso(), depoimentoEncontrado.getEgresso().getIdEgresso());
    }

    @Test
    public void deveGerarErroAoBuscarDepoimentoPorIdInexistente() {
        assertThrows(EntityNotFoundException.class, () -> service.buscarDepoimentoPorId(100));
    }
    @Test
    public void deveGerarErroAoBuscarDepoimentoPorIdNull() {
        assertThrows(IllegalArgumentException.class, () -> service.buscarDepoimentoPorId(null));
    }
    @Test
    @Transactional
    public void deveBuscarDepoimentosPorAnoFluxoPadrao() {
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
                .data(LocalDate.now().plusYears(-1))
                .egresso(egressoBase)
                .texto("depoimento antigo")
                .build());

        List<Depoimento> depoimentos = service.buscarDepoimentosPorAno(2025);
        List<Depoimento> semDepoimentos = service.buscarDepoimentosPorAno(2024);

        assertEquals(3, depoimentos.size());
        assertEquals(1, semDepoimentos.size());
    }
    @Test
    public void deveGerarErroAoBuscarDepoimentoPorAnoNull() {
        assertThrows(IllegalArgumentException.class, () -> service.buscarDepoimentosPorAno(null));
    }
    @Test
    public void deveGerarErroAoBuscarDepoimentoPorAnoNegativo() {
        assertThrows(IllegalArgumentException.class, () -> service.buscarDepoimentosPorAno(-1));
    }
    @Test
    public void deveGerarErroAoBuscarDepoimentoPorAnoMaiorQueAnoAtual() {
        assertThrows(IllegalArgumentException.class, () -> service.buscarDepoimentosPorAno(LocalDate.now().getYear() + 1));
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
    @Test
    @Transactional
    public void deveBuscarDepoimentosPorEgresso() {
        Depoimento depoimento1 = service.salvarDepoimento(SalvarDepoimentoUC.Request.builder()
                .idEgresso(egressoBase.getIdEgresso())
                .texto("depoimento padrao")
                .build());
        Depoimento depoimento2 = service.salvarDepoimento(SalvarDepoimentoUC.Request.builder()
                .idEgresso(egressoBase.getIdEgresso())
                .texto("depoimento padrao 2")
                .build());
        Depoimento depoimento3 = service.salvarDepoimento(SalvarDepoimentoUC.Request.builder()
                .idEgresso(egressoBase.getIdEgresso())
                .texto("depoimento padrao 3")
                .build());
        Depoimento depoimento4 = service.salvarDepoimento(SalvarDepoimentoUC.Request.builder()
                .idEgresso(egressoBase2.getIdEgresso())
                .texto("depoimento padrao 4")
                .build());
        entityManager.clear();
        List<Depoimento> depoimentosEncontrados = service.buscarDepoimentoPorEgresso(egressoBase.getIdEgresso());
        List<Depoimento> depoimentosEncontrados2 = service.buscarDepoimentoPorEgresso(egressoBase2.getIdEgresso());

        assertEquals(3, depoimentosEncontrados.size());
        assertEquals(1, depoimentosEncontrados2.size());
    }
    @Test
    public void deveGerarErroAoBuscarDepoimentosPorEgressoIdNull() {
        assertThrows(IllegalArgumentException.class, () -> service.buscarDepoimentoPorEgresso(null));
    }
    @Test
    public void deveGerarErroAoBuscarDepoimentosPorEgressoIdInexistente() {
        assertThrows(EntityNotFoundException.class, () -> service.buscarDepoimentoPorEgresso(100));
    }
    @Test
    @Transactional
    public void deveAtualizarDepoimentoFluxoPadrao() {
        Depoimento depoimentoCriado = criarDepoimentoTestes();

        UpdateDepoimentoUC.Request request = UpdateDepoimentoUC.Request.builder()
                .texto("depoimento atualizado")
                .build();

        service.updateDepoimento(depoimentoCriado.getIdDepoimento(), request);

        Depoimento depoimentoEncontrado = depoimentoService.buscarDepoimentoPorId(depoimentoCriado.getIdDepoimento());

        assertNotNull(depoimentoEncontrado);
        assertEquals(depoimentoCriado.getIdDepoimento(), depoimentoEncontrado.getIdDepoimento());
        assertEquals(request.getTexto(), depoimentoEncontrado.getTexto());
        assertEquals(LocalDate.now(), depoimentoEncontrado.getData());
        assertEquals(depoimentoCriado.getEgresso().getIdEgresso(), depoimentoEncontrado.getEgresso().getIdEgresso());
    }
    @Test
    public void deveGerarErroAoAtualizarDepoimentoIdInexistente(){
        UpdateDepoimentoUC.Request request = UpdateDepoimentoUC.Request.builder().texto("depoimento atualizado").build();
        assertThrows(EntityNotFoundException.class, () -> service.updateDepoimento(100, request));
    }
    @Test
    public void deveGerarErroAoAtualizarDepoimentoNull() {
        UpdateDepoimentoUC.Request request = UpdateDepoimentoUC.Request.builder().texto("depoimento atualizado").build();
        assertThrows(IllegalArgumentException.class, () -> service.updateDepoimento(null, request));
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

    private Depoimento criarDepoimentoTestes() {
        SalvarDepoimentoUC.Request depoimento = SalvarDepoimentoUC.Request.builder()
                .idEgresso(egressoBase.getIdEgresso())
                .texto("depoimento padrao")
                .build();

        return service.salvarDepoimento(depoimento);
    }
}
