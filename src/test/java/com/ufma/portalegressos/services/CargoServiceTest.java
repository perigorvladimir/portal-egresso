package com.ufma.portalegressos.services;

import com.ufma.portalegressos.application.domain.Cargo;
import com.ufma.portalegressos.application.domain.Egresso;
import com.ufma.portalegressos.application.out.CargoJpaRepository;
import com.ufma.portalegressos.application.out.EgressoJpaRepository;
import com.ufma.portalegressos.application.services.CargoService;
import com.ufma.portalegressos.application.services.EgressoService;
import com.ufma.portalegressos.application.usecases.cargo.CargoUC;
import com.ufma.portalegressos.application.usecases.cargo.SalvarCargoUC;
import jakarta.persistence.EntityNotFoundException;
import org.assertj.core.api.OptionalAssert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Profile("test")
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

    @Test
    @Transactional
    public void deveSalvarCargo(){
        SalvarCargoUC.Request cargo = SalvarCargoUC.Request.builder()
                .local("UFMA")
                .anoInicio(2021)
                .anoFim(2022)
                .descricao("Cargo")
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

        Assertions.assertThrows(IllegalArgumentException.class, () -> service.salvar(cargo));
    }

    @Test
    @Transactional
    public void deveBuscarCargoPorIdPadrao(){
        Cargo cargoCriado = service.salvar(SalvarCargoUC.Request.builder()
                .local("UFMA")
                .anoInicio(2021)
                .anoFim(2022)
                .descricao("Cargo")
                .idEgresso(egressoBase.getIdEgresso())
                .build());

        Cargo cargoEncontrado = service.buscarPorId(cargoCriado.getIdCargo());

        assertEquals(cargoCriado.getIdCargo(), cargoEncontrado.getIdCargo());
        assertEquals(cargoCriado.getEgresso(), cargoEncontrado.getEgresso());
    }

    @Test
    public void deveGerarErroAoBuscarCargoPorIdInexistente(){
        assertThrows(EntityNotFoundException.class, () -> service.buscarPorId(1));
    }

    @Test
    public void deveDeletarCargoPadrao(){
        Cargo cargoCriado = service.salvar(SalvarCargoUC.Request.builder()
                .local("UFMA")
                .anoInicio(2021)
                .anoFim(2022)
                .descricao("Cargo")
                .idEgresso(egressoBase.getIdEgresso())
                .build());

        service.deletar(cargoCriado.getIdCargo());

        Optional<Cargo> cargoEncontrado = cargoJpaRepository.findById(cargoCriado.getIdCargo());
        assertTrue(cargoEncontrado.isEmpty());
    }

    @Test
    public void naoDeveGerarErroAoDeletarCargoInexistente(){
        assertDoesNotThrow(() -> service.deletar(1));
    }
}
