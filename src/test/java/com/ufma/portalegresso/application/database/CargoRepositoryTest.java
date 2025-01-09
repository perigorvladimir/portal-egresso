package com.ufma.portalegresso.application.database;

import com.ufma.portalegresso.application.domain.Cargo;
import com.ufma.portalegresso.application.domain.Egresso;
import com.ufma.portalegresso.application.out.CargoJpaRepository;
import com.ufma.portalegresso.application.out.EgressoJpaRepository;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Profile("test")
public class CargoRepositoryTest {
    @Autowired
    private CargoJpaRepository cargoJpaRepository;
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
    public void deveVerificarSalvarCargo(){
        Cargo cargo = Cargo.builder()
                .egresso(egressoBase)
                .anoInicio(2022)
                .anoFim(2023)
                .descricao("descricao teste cargo")
                .local("local teste cargo")
                .build();

        Cargo cargoSalvo = cargoJpaRepository.save(cargo);

        assertNotNull(cargoSalvo);
        assertEquals(cargo.getEgresso(), cargoSalvo.getEgresso());
        assertEquals(cargo.getLocal(), cargoSalvo.getLocal());
        assertEquals(cargo.getDescricao(), cargoSalvo.getDescricao());
        assertEquals(cargo.getAnoInicio(), cargoSalvo.getAnoInicio());
        assertEquals(cargo.getAnoFim(), cargoSalvo.getAnoFim());
    }
    @Test
    @Transactional
    public void deveVerificarBuscarCargoPorIdCargo(){
        Cargo cargoCriado1 = cargoJpaRepository.save(Cargo.builder()
                .egresso(egressoBase)
                .anoInicio(2022)
                .anoFim(2023)
                .descricao("descricao teste cargo")
                .local("local teste cargo")
                .build());
        Cargo cargoCriado2 = cargoJpaRepository.save(Cargo.builder()
                .egresso(egressoBase)
                .anoInicio(2021)
                .anoFim(2024)
                .descricao("descricao teste cargo 2")
                .local("local teste cargo 2")
                .build());

        Optional<Cargo> resposta1 = cargoJpaRepository.findById(cargoCriado1.getIdCargo());
        Optional<Cargo> resposta2 = cargoJpaRepository.findById(cargoCriado2.getIdCargo());

        // resultado1
        assertTrue(resposta1.isPresent());
        Cargo cargoEncontrado1 = resposta1.get();
        assertEquals(cargoCriado1.getIdCargo(), cargoEncontrado1.getIdCargo());
        assertEquals(cargoCriado1.getDescricao(), cargoEncontrado1.getDescricao());

        //resultado 2
        assertTrue(resposta2.isPresent());
        Cargo cargoEncontrado2 = resposta2.get();
        assertEquals(cargoCriado2.getIdCargo(), cargoEncontrado2.getIdCargo());
        assertEquals(cargoCriado2.getDescricao(), cargoEncontrado2.getDescricao());
    }

    @Test
    public void naoDeveSalvarCargoSemEgressoOuDescricaoOuLocalOuAnoInicio(){
        Cargo cargo = Cargo.builder()
                .local("localteste")
                .descricao("descricao teste")
                .anoInicio(2021)
                .anoFim(2024)
                .build();
        assertThrows(ConstraintViolationException.class, () -> cargoJpaRepository.save(cargo));
        cargo.setEgresso(egressoBase);
        cargo.setLocal(null);
        assertThrows(ConstraintViolationException.class, () -> cargoJpaRepository.save(cargo));
        cargo.setLocal("local teste");
        cargo.setDescricao(null);
        assertThrows(ConstraintViolationException.class, () -> cargoJpaRepository.save(cargo));
        cargo.setDescricao("descricao teste");
        cargo.setAnoInicio(null);
        assertThrows(ConstraintViolationException.class, () -> cargoJpaRepository.save(cargo));
    }

    @Test
    @Transactional
    public void deveVerificarBuscarTodosCargos(){
        Cargo cargo1 = Cargo.builder()
                .egresso(egressoBase)
                .descricao("descricao teste 1")
                .local("local teste 1")
                .anoInicio(2015)
                .build();
        Cargo cargo2 = Cargo.builder()
                .egresso(egressoBase)
                .descricao("descricao teste 2")
                .local("local teste 2")
                .anoInicio(2015)
                .anoFim(2019)
                .build();
        Cargo cargo3 = Cargo.builder()
                .egresso(egressoBase)
                .descricao("descricao teste 3")
                .local("local teste 3")
                .anoInicio(2020)
                .anoFim(2030)
                .build();
        cargoJpaRepository.save(cargo1);
        cargoJpaRepository.save(cargo2);
        cargoJpaRepository.save(cargo3);

        List<Cargo> resposta = cargoJpaRepository.findAll();

        assertFalse(resposta.isEmpty());
        assertEquals(3, resposta.size());
        assertTrue(resposta.contains(cargo1));
    }
}
