package com.ufma.portalegresso.application.services;

import com.ufma.portalegresso.application.domain.Cargo;
import com.ufma.portalegresso.application.domain.Egresso;
import com.ufma.portalegresso.application.out.CargoJpaRepository;
import com.ufma.portalegresso.application.out.EgressoJpaRepository;
import com.ufma.portalegresso.application.usecases.cargo.SalvarCargoUC;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class CargoServiceTest {
    @Autowired
    private CargoService service;
    private static Egresso egressoBase;
    @Autowired
    private CargoJpaRepository cargoJpaRepository;

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
    public void deveSalvarCargo(){
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
    public void deveGerarErroAoTentarSalvarCargoComAnoFimMenorQueAnoInicio(){
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
    public void deveGerarErroAoTentarSalvarCargoSemEgresso(){
        SalvarCargoUC.Request cargo = SalvarCargoUC.Request.builder()
                .local("UFMA")
                .anoInicio(2021)
                .anoFim(2022)
                .descricao("Cargo")
                .build();

        assertThrows(InvalidDataAccessApiUsageException.class, () -> service.salvar(cargo));
    }

    @Test
    @Transactional
    public void deveBuscarCargoPorIdPadrao(){
        Cargo cargoCriado = service.salvar(SalvarCargoUC.Request.builder()
                .local("UFMA")
                .anoInicio(2021)
                .anoFim(2022)
                .descricao("Cargo")
                .tipoAreaTrabalho("RH")
                .idEgresso(egressoBase.getIdEgresso())
                .build());

        Cargo cargoEncontrado = service.buscarPorId(cargoCriado.getIdCargo());

        assertEquals(cargoCriado.getIdCargo(), cargoEncontrado.getIdCargo());
        assertEquals(cargoCriado.getEgresso(), cargoEncontrado.getEgresso());
    }

    @Test
    public void deveGerarErroAoBuscarCargoPorIdInexistente(){
        assertThrows(EntityNotFoundException.class, () -> service.buscarPorId(100));
    }

    @Test
    public void deveDeletarCargoPadrao(){
        Cargo cargoCriado = service.salvar(SalvarCargoUC.Request.builder()
                .local("UFMA")
                .anoInicio(2021)
                .anoFim(2022)
                .descricao("Cargo")
                .tipoAreaTrabalho("FINANCEIRO")
                .idEgresso(egressoBase.getIdEgresso())
                .build());

        service.deletarPorId(cargoCriado.getIdCargo());

        assertThrows(EntityNotFoundException.class, () -> service.buscarPorId(cargoCriado.getIdCargo()));
    }

    @Test
    public void naoDeveGerarErroAoDeletarCargoInexistente(){
        assertDoesNotThrow(() -> service.deletarPorId(1));
    }
}
