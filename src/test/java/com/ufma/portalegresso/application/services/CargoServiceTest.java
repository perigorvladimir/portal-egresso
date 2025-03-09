package com.ufma.portalegresso.application.services;

import com.ufma.portalegresso.application.domain.Cargo;
import com.ufma.portalegresso.application.domain.Egresso;
import com.ufma.portalegresso.application.out.EgressoJpaRepository;
import com.ufma.portalegresso.application.usecases.cargo.*;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
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
public class CargoServiceTest {
    @Autowired
    private CargoService service;
    private static Egresso egressoBase;

    @BeforeAll
    public static void setUp(@Autowired EgressoJpaRepository egressoJpaRepository){
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
    public static void cleanUp(@Autowired EgressoJpaRepository egressoJpaRepository){
        egressoJpaRepository.deleteById(egressoBase.getIdEgresso());
    }
    @Test
    @Transactional
    public void deveSalvarCargoFluxoPadrao(){
        SalvarCargoUC.Request cargo = SalvarCargoUC.Request.builder()
                .local("UFMA")
                .anoInicio(2021)
                .anoFim(2022)
                .descricao("Cargo")
                .tipoAreaTrabalho("FINANCEIRO")
                .idEgresso(egressoBase.getIdEgresso())
                .build();

        Cargo cargoSalvo = service.salvar(cargo);

        assertNotNull(cargoSalvo);
        assertNotNull(cargoSalvo.getIdCargo());
        assertEquals(cargo.getDescricao(), cargoSalvo.getDescricao());
        assertEquals(cargo.getAnoInicio(), cargoSalvo.getAnoInicio());
        assertEquals(cargo.getAnoFim(), cargoSalvo.getAnoFim());
        assertEquals(cargo.getLocal(), cargoSalvo.getLocal());
    }

    @Test
    public void deveGerarErroAoSalvarCargoComAnoFimMenorQueAnoInicio(){
        SalvarCargoUC.Request cargo = SalvarCargoUC.Request.builder()
                .local("UFMA")
                .anoInicio(2021)
                .anoFim(2020)
                .descricao("Cargo")
                .idEgresso(egressoBase.getIdEgresso())
                .build();

        assertThrows(IllegalArgumentException.class, () -> service.salvar(cargo));
    }

    @Test
    @Transactional
    public void deveTestarCamposQuePodemSerNulos(){
        SalvarCargoUC.Request cargo = SalvarCargoUC.Request.builder()
                .local("UFMA")
                .anoInicio(2021)
                .anoFim(null)
                .descricao("Cargo")
                .idEgresso(egressoBase.getIdEgresso())
                .tipoAreaTrabalho("RH")
                .build();

        assertDoesNotThrow(() -> service.salvar(cargo));
    }

    @Test
    @Transactional
    public void deveGerarErroDeIntegridadeAoPassarAnoInicioNulo(){//comparacao entre anos
        SalvarCargoUC.Request cargo = SalvarCargoUC.Request.builder()
                .local("UFMA")
                .anoFim(2022)
                .descricao("Cargo")
                .idEgresso(egressoBase.getIdEgresso())
                .tipoAreaTrabalho("RH")
                .build();

        assertThrows(DataIntegrityViolationException.class, () -> service.salvar(cargo));
    }

    @Test
    @Transactional
    public void deveGerarErroAoSalvarCargoMandandoTipoAreaTrabalhoInexistenteOuNulo(){
        SalvarCargoUC.Request cargoTipoAreaNull = SalvarCargoUC.Request.builder().tipoAreaTrabalho(null).local("UFMA").anoInicio(2021).anoFim(2022).descricao("Cargo").idEgresso(egressoBase.getIdEgresso()).build();

        SalvarCargoUC.Request cargoTipoAreaInexistente = SalvarCargoUC.Request.builder().tipoAreaTrabalho("rh").local("UFMA").anoInicio(2021).anoFim(2022).descricao("Cargo").idEgresso(egressoBase.getIdEgresso()).build();

        assertThrows(DataIntegrityViolationException.class, () -> service.salvar(cargoTipoAreaNull));
        assertThrows(IllegalArgumentException.class, () -> service.salvar(cargoTipoAreaInexistente));
    }

    @Test
    public void deveGerarErroAoTentarSalvarCargoSemEgresso(){
        SalvarCargoUC.Request cargo = SalvarCargoUC.Request.builder()
                .local("UFMA")
                .anoInicio(2021)
                .anoFim(2022)
                .descricao("Cargo")
                .build();

        assertThrows(IllegalArgumentException.class, () -> service.salvar(cargo));
    }

    @Test
    @Transactional
    public void deveGerarErroAoTentarSalvarCargoComEgressoInexistente(){
        SalvarCargoUC.Request cargo = SalvarCargoUC.Request.builder()
                .local("UFMA")
                .anoInicio(2021)
                .anoFim(2022)
                .descricao("Cargo")
                .idEgresso(100)
                .build();

        assertThrows(EntityNotFoundException.class, () -> service.salvar(cargo));
    }

    @Test
    @Transactional
    public void deveBuscarCargoPorIdPadrao(){
        Cargo cargoCriado = criarCargoTestes();

        Cargo cargoEncontrado = service.buscarPorId(cargoCriado.getIdCargo());

        assertEquals(cargoCriado.getIdCargo(), cargoEncontrado.getIdCargo());
        assertEquals(cargoCriado.getEgresso(), cargoEncontrado.getEgresso());
    }

    @Test
    public void deveGerarErroAoBuscarCargoPorIdInexistente(){
        assertThrows(EntityNotFoundException.class, () -> service.buscarPorId(100));
    }
    @Test
    public void deveGerarErroAoBuscarCargoPorIdNull(){
        assertThrows(IllegalArgumentException.class, () -> service.buscarPorId(null));
    }

    @Test
    @Transactional
    public void deveBuscarTodosCargos(){
        Cargo cargoCriado = service.salvar(SalvarCargoUC.Request.builder().local("UFMA").anoInicio(2021).anoFim(2022).descricao("Cargo").tipoAreaTrabalho("RH").idEgresso(egressoBase.getIdEgresso()).build());

        Cargo cargoCriado2 = service.salvar(SalvarCargoUC.Request.builder().local("slz").anoInicio(2021).anoFim(2024).descricao("Cargo 2").tipoAreaTrabalho("FINANCEIRO").idEgresso(egressoBase.getIdEgresso()).build());

        List<Cargo> resposta = service.buscarTodosCargos();

        assertFalse(resposta.isEmpty());
        assertEquals(2, resposta.size());
        assertTrue(resposta.contains(cargoCriado));
        assertTrue(resposta.contains(cargoCriado2));
    }

    @Test
    public void deveDeletarCargoPorId(){
        Cargo cargoCriado = criarCargoTestes();

        service.deletarPorId(cargoCriado.getIdCargo());

        assertThrows(EntityNotFoundException.class, () -> service.buscarPorId(cargoCriado.getIdCargo()));
    }

    @Test
    public void naoDeveGerarErroAoDeletarCargoInexistente(){
        assertDoesNotThrow(() -> service.deletarPorId(1));
    }

    @Test
    @Transactional
    public void deveAtualizarCargoFluxoPadrao(){
        Cargo cargoCriado = criarCargoTestes();

        UpdateCargoUC.Request cargoAtualizado = UpdateCargoUC.Request.builder()
                .local("slz")
                .anoInicio(2023)
                .anoFim(null)
                .descricao("Cargo 2")
                .tipoAreaTrabalho("FINANCEIRO")
                .build();

        service.updateCargo(cargoCriado.getIdCargo(), cargoAtualizado);

        Cargo cargoEncontrado = service.buscarPorId(cargoCriado.getIdCargo());

        assertEquals(cargoAtualizado.getDescricao(), cargoEncontrado.getDescricao());
        assertEquals(cargoAtualizado.getAnoInicio(), cargoEncontrado.getAnoInicio());
        assertEquals(cargoAtualizado.getAnoFim(), cargoEncontrado.getAnoFim());
        assertEquals(cargoAtualizado.getLocal(), cargoEncontrado.getLocal());
        assertEquals(cargoAtualizado.getTipoAreaTrabalho(), cargoEncontrado.getTipoAreaTrabalho().toString());
    }

    @Test
    @Transactional
    public void deveGerarErroAoAtualizarCargoInexistente(){

        UpdateCargoUC.Request cargoAtualizado = UpdateCargoUC.Request.builder()
                .local("slz")
                .anoInicio(2022)
                .anoFim(2023)
                .descricao("Cargo 2")
                .tipoAreaTrabalho("FINANCEIRO")
                .build();

        assertThrows(EntityNotFoundException.class, () -> service.updateCargo(100, cargoAtualizado));
    }

    @Test
    @Transactional
    public void deveGerarErroAoAtualizarCargoMandandoIdNull(){
        UpdateCargoUC.Request cargoAtualizado = UpdateCargoUC.Request.builder()
                .local("slz")
                .anoInicio(2022)
                .anoFim(2023)
                .descricao("Cargo 2")
                .tipoAreaTrabalho("FINANCEIRO")
                .build();

        assertThrows(IllegalArgumentException.class, () -> service.updateCargo(null, cargoAtualizado));
    }

    @Test
    @Transactional
    public void deveGerarErroAoAtualizarCargoComAnoFimMenorQueAnoInicio(){
        Cargo cargoCriado = criarCargoTestes();

        UpdateCargoUC.Request cargoAtualizado = UpdateCargoUC.Request.builder()
                .local("slz")
                .anoInicio(2022)
                .anoFim(2019)
                .descricao("Cargo 2")
                .tipoAreaTrabalho("FINANCEIRO")
                .build();

        assertThrows(IllegalArgumentException.class, () -> service.updateCargo(cargoCriado.getIdCargo(), cargoAtualizado));
    }

    @Test
    @Transactional
    public void deveGerarErroAoAtualizarCargoMandandoTipoAreaTrabalhoInexistenteOuNulo(){
        Cargo cargoCriado = criarCargoTestes();

        UpdateCargoUC.Request cargoAtualizadoAreaNull = UpdateCargoUC.Request.builder()
                .local("slz")
                .anoInicio(2011)
                .anoFim(null)
                .descricao("Cargo 2")
                .tipoAreaTrabalho(null)
                .build();
        UpdateCargoUC.Request cargoAtualizadoAreaInexistente = UpdateCargoUC.Request.builder()
                .local("slz")
                .anoInicio(2015)
                .anoFim(2019)
                .descricao("Cargo 2")
                .tipoAreaTrabalho("rh")
                .build();
        DataIntegrityViolationException exceptionAreaNull = assertThrows(DataIntegrityViolationException.class, () -> service.updateCargo(cargoCriado.getIdCargo(), cargoAtualizadoAreaNull));
        assertEquals("tipo_area_trabalho", exceptionAreaNull.getCause().getCause().getLocalizedMessage().split("\"")[1].toLowerCase());
        assertThrows(IllegalArgumentException.class, () -> service.updateCargo(cargoCriado.getIdCargo(), cargoAtualizadoAreaInexistente));
    }

    private Cargo criarCargoTestes(){
        return service.salvar(SalvarCargoUC.Request.builder()
                .local("UFMA")
                .anoInicio(2021)
                .anoFim(2022)
                .descricao("Cargo")
                .tipoAreaTrabalho("RH")
                .idEgresso(egressoBase.getIdEgresso())
                .build());
    }
}
