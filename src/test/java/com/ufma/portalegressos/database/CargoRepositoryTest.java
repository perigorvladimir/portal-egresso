package com.ufma.portalegressos.database;

import com.ufma.portalegressos.database.entities.CargoEntity;
import com.ufma.portalegressos.database.entities.EgressoEntity;
import com.ufma.portalegressos.database.repositories.CargoJpaRepository;
import com.ufma.portalegressos.database.repositories.EgressoJpaRepository;
import jakarta.validation.ConstraintViolationException;
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
    private static EgressoEntity egressoBase;
    @BeforeAll
    public static void setUp(@Autowired EgressoJpaRepository egressoJpaRepository){
        EgressoEntity egresso = EgressoEntity.builder()
                .nome("egresso para teste")
                .email("egressoparateste@gmail.com")
                .descricao("descricao egresso para teste")
                .curriculo("curriculo egresso para teste")
                .Instagram("egresso_teste")
                .linkedin("https://linkedin/egressoTeste")
                .foto("foto codificada")
                .build();
        egressoBase = egressoJpaRepository.save(egresso);
    }
    @Test
    @Transactional
    public void deveVerificarSalvarCargo(){
        CargoEntity cargo = CargoEntity.builder()
                .egresso(egressoBase)
                .anoInicio(2022)
                .anoFim(2023)
                .descricao("descricao teste cargo")
                .local("local teste cargo")
                .build();

        CargoEntity cargoSalvo = cargoJpaRepository.save(cargo);

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
        CargoEntity cargoCriado1 = cargoJpaRepository.save(CargoEntity.builder()
                .egresso(egressoBase)
                .anoInicio(2022)
                .anoFim(2023)
                .descricao("descricao teste cargo")
                .local("local teste cargo")
                .build());
        CargoEntity cargoCriado2 = cargoJpaRepository.save(CargoEntity.builder()
                .egresso(egressoBase)
                .anoInicio(2021)
                .anoFim(2024)
                .descricao("descricao teste cargo 2")
                .local("local teste cargo 2")
                .build());

        Optional<CargoEntity> resposta1 = cargoJpaRepository.findById(cargoCriado1.getIdCargo());
        Optional<CargoEntity> resposta2 = cargoJpaRepository.findById(cargoCriado2.getIdCargo());

        // resultado1
        assertTrue(resposta1.isPresent());
        CargoEntity cargoEncontrado1 = resposta1.get();
        assertEquals(cargoCriado1.getIdCargo(), cargoEncontrado1.getIdCargo());
        assertEquals(cargoCriado1.getDescricao(), cargoEncontrado1.getDescricao());

        //resultado 2
        assertTrue(resposta2.isPresent());
        CargoEntity cargoEncontrado2 = resposta2.get();
        assertEquals(cargoCriado2.getIdCargo(), cargoEncontrado2.getIdCargo());
        assertEquals(cargoCriado2.getDescricao(), cargoEncontrado2.getDescricao());
    }

    @Test
    public void naoDeveSalvarCargoSemEgressoOuDescricaoOuLocalOuAnoInicio(){
        CargoEntity cargoEntity = CargoEntity.builder()
                .local("localteste")
                .descricao("descricao teste")
                .anoInicio(2021)
                .anoFim(2024)
                .build();
        assertThrows(ConstraintViolationException.class, () -> cargoJpaRepository.save(cargoEntity));
        cargoEntity.setEgresso(egressoBase);
        cargoEntity.setLocal(null);
        assertThrows(ConstraintViolationException.class, () -> cargoJpaRepository.save(cargoEntity));
        cargoEntity.setLocal("local teste");
        cargoEntity.setDescricao(null);
        assertThrows(ConstraintViolationException.class, () -> cargoJpaRepository.save(cargoEntity));
        cargoEntity.setDescricao("descricao teste");
        cargoEntity.setAnoInicio(null);
        assertThrows(ConstraintViolationException.class, () -> cargoJpaRepository.save(cargoEntity));
    }

    @Test
    @Transactional
    public void deveVerificarBuscarTodosCargos(){
        CargoEntity cargo1 = CargoEntity.builder()
                .egresso(egressoBase)
                .descricao("descricao teste 1")
                .local("local teste 1")
                .anoInicio(2015)
                .build();
        CargoEntity cargo2 = CargoEntity.builder()
                .egresso(egressoBase)
                .descricao("descricao teste 2")
                .local("local teste 2")
                .anoInicio(2015)
                .anoFim(2019)
                .build();
        CargoEntity cargo3 = CargoEntity.builder()
                .egresso(egressoBase)
                .descricao("descricao teste 3")
                .local("local teste 3")
                .anoInicio(2020)
                .anoFim(2030)
                .build();
        cargoJpaRepository.save(cargo1);
        cargoJpaRepository.save(cargo2);
        cargoJpaRepository.save(cargo3);

        List<CargoEntity> resposta = cargoJpaRepository.findAll();

        assertFalse(resposta.isEmpty());
        assertEquals(3, resposta.size());
        assertTrue(resposta.contains(cargo1));
    }
}
