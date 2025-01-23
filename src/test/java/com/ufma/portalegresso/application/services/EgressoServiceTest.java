package com.ufma.portalegresso.application.services;

import com.ufma.portalegresso.application.domain.Egresso;
import com.ufma.portalegresso.application.usecases.egresso.SalvarEgressoUC;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class EgressoServiceTest {
    @Autowired
    private EgressoService service;

    @Test
    @Transactional
    public void deveSalvarEgressoFluxoPadrao() {
        SalvarEgressoUC.Request egresso = SalvarEgressoUC.Request.builder()
                .nome("Igor")
                .email("QYv3I@example.com")
                .descricao("Descrição")
                .foto("Foto")
                .linkedin("Linkedin")
                .instagram("Instagram")
                .curriculo("Curriculo")
                .build();

        Egresso egressoSalvo = service.salvarEgresso(egresso);

        assertNotNull(egressoSalvo);
        assertNotNull(egressoSalvo.getIdEgresso());
        assertEquals(egresso.getNome(), egressoSalvo.getNome());
        assertEquals(egresso.getEmail(), egressoSalvo.getEmail());
        assertEquals(egresso.getDescricao(), egressoSalvo.getDescricao());
        assertEquals(egresso.getFoto(), egressoSalvo.getFoto());
        assertEquals(egresso.getLinkedin(), egressoSalvo.getLinkedin());
        assertEquals(egresso.getInstagram(), egressoSalvo.getInstagram());
        assertEquals(egresso.getCurriculo(), egressoSalvo.getCurriculo());
    }

    @Test
    public void deveGerarErroAoSalvarEgressoSemNomeOuEmail() {
        SalvarEgressoUC.Request egresso = SalvarEgressoUC.Request.builder()
                .email("QYv3I@example.com")
                .descricao("Descrição")
                .linkedin("Linkedin")
                .instagram("Instagram")
                .curriculo("Curriculo")
                .build();
        assertThrows(DataIntegrityViolationException.class, () -> service.salvarEgresso(egresso));

        SalvarEgressoUC.Request egresso2 = SalvarEgressoUC.Request.builder()
                .nome("Geraldo")
                .descricao("Descrição")
                .linkedin("Linkedin")
                .instagram("Instagram")
                .curriculo("Curriculo")
                .build();
        assertThrows(DataIntegrityViolationException.class, () -> service.salvarEgresso(egresso2));
    }

    @Test
    @Transactional
    public void deveBuscarEgressoPorIdFluxoPadrao() {
        Egresso egressoCriado = service.salvarEgresso(SalvarEgressoUC.Request.builder()
                .nome("Igor")
                .email("QYv3I@example.com")
                .descricao("Descrição")
                .foto("Foto")
                .linkedin("Linkedin")
                .instagram("Instagram")
                .curriculo("Curriculo")
                .build());

        Egresso egressoBuscado = service.buscarEgressoPorId(egressoCriado.getIdEgresso());

        assertNotNull(egressoBuscado);
        assertEquals(egressoCriado.getIdEgresso(), egressoBuscado.getIdEgresso());
        assertEquals(egressoCriado.getNome(), egressoBuscado.getNome());
        assertEquals(egressoCriado.getEmail(), egressoBuscado.getEmail());
        assertEquals(egressoCriado.getDescricao(), egressoBuscado.getDescricao());
    }

    @Test
    public void deveGerarErroAoBuscarEgressoPorIdInexistente() {
        assertThrows(EntityNotFoundException.class, () -> service.buscarEgressoPorId(100));
    }

    /*@Test
    @Transactional
    public void deveBuscarEgressosPorCursoFluxoPadrao() {
        Egresso egressoCriado = service.salvarEgresso(SalvarEgressoUC.Request.builder()
                .nome("Igor")
                .email("QYv3I@example.com")
                .descricao("Descrição")
                .build());
        Egresso egressoCriado2 = service.salvarEgresso(SalvarEgressoUC.Request.builder()
                .nome("Igor")
                .email("QYv3I@example.com")
                .descricao("Descrição")
                .build());
        Egresso egressoCriado3 = service.salvarEgresso(SalvarEgressoUC.Request.builder()
                .nome("Igor")
                .email("QYv3I@example.com")
                .descricao("Descrição")
                .build());
        Egresso egressoCriado4 = service.salvarEgresso(SalvarEgressoUC.Request.builder()
                .nome("Igor")
                .email("QYv3I@example.com")
    }*/

    @Test
    @Transactional
    public void deveBuscarTodosEgressos() {
        Egresso egressoCriado1 = service.salvarEgresso(SalvarEgressoUC.Request.builder()
                .nome("Igor")
                .email("QYv3I@example.com")
                .descricao("Descrição")
                .instagram("Instagram")
                .build());
        Egresso egressoCriado2 = service.salvarEgresso(SalvarEgressoUC.Request.builder()
                .nome("Rick")
                .email("exemplo@example.com")
                .descricao("Descrição")
                .linkedin("Linkedin")
                .build());
        Egresso egressoCriado3 = service.salvarEgresso(SalvarEgressoUC.Request.builder()
                .nome("Igor")
                .email("exemplo2@example.com")
                .descricao("Descrição")
                .foto("Foto")
                .build());

        List<Egresso> egressos = service.buscarTodosEgressos();

        assertFalse(egressos.isEmpty());
        assertEquals(3, egressos.size());
        assertTrue(egressos.contains(egressoCriado1));
        assertTrue(egressos.contains(egressoCriado2));
        assertTrue(egressos.contains(egressoCriado3));
    }

    @Test
    public void deveDeletarEgressoFluxoPadrao() {
        Egresso egressoCriado = service.salvarEgresso(SalvarEgressoUC.Request.builder()
                .nome("Igor")
                .email("QYv3I@example.com")
                .descricao("Descrição")
                .build());

        service.deletarEgressoPorId(egressoCriado.getIdEgresso());


        assertThrows(EntityNotFoundException.class, () -> service.buscarEgressoPorId(egressoCriado.getIdEgresso()));
    }
    @Test
    public void naoDeveGerarErroAoDeletarCargoInexistente(){
        assertDoesNotThrow(() -> service.deletarEgressoPorId(100));
    }
}
