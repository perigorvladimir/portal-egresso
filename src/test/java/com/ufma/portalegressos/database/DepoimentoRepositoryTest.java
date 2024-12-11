package com.ufma.portalegressos.database;

import com.ufma.portalegressos.infrastructure.entities.DepoimentoEntity;
import com.ufma.portalegressos.infrastructure.entities.EgressoEntity;
import com.ufma.portalegressos.infrastructure.repositories.DepoimentoJpaRepository;
import com.ufma.portalegressos.infrastructure.repositories.EgressoJpaRepository;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Profile("test")
public class DepoimentoRepositoryTest {
    @Autowired
    private DepoimentoJpaRepository depoimentoJpaRepository;
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
    public void deveVerificarSalvarDepoimento(){
        // CENARIO
        DepoimentoEntity depoimentoEntity = DepoimentoEntity.builder()
                .egresso(egressoBase)
                .texto("texto salvar depoimento teste")
                .data(LocalDate.now())
                .build();
        // ACAO
        DepoimentoEntity depoimentoSalvo = depoimentoJpaRepository.save(depoimentoEntity);
        // VERIFICACAO
        assertNotNull(depoimentoSalvo);
        assertEquals(depoimentoEntity.getEgresso(),depoimentoSalvo.getEgresso());
        assertEquals(depoimentoEntity.getTexto(),depoimentoSalvo.getTexto());
        assertEquals(depoimentoEntity.getData(),depoimentoSalvo.getData());
    }

    @Test
    @Transactional
    public void deveVerificarBuscarDepoimentoPorId(){
        // CENARIO
        Integer idDepoimentoCriado1 = depoimentoJpaRepository.save(DepoimentoEntity.builder()
                .egresso(egressoBase)
                .texto("depoimento teste 1")
                .data(LocalDate.now())
                .build()).getIdDepoimento();
        Integer idDepoimentoCriado2 = depoimentoJpaRepository.save(DepoimentoEntity.builder()
                .egresso(egressoBase)
                .texto("depoimento teste 2")
                .data(LocalDate.now())
                .build()).getIdDepoimento();
        // ACAO
        Optional<DepoimentoEntity> resultado1 = depoimentoJpaRepository.findById(idDepoimentoCriado1);
        Optional<DepoimentoEntity> resultado2 = depoimentoJpaRepository.findById(idDepoimentoCriado2);
        // VALIDAÇÃO
        assertTrue(resultado1.isPresent());
        DepoimentoEntity depoimentoEncontrado1 = resultado1.get();
        assertEquals(idDepoimentoCriado1, depoimentoEncontrado1.getIdDepoimento());
        assertEquals("depoimento teste 1", depoimentoEncontrado1.getTexto());

        assertTrue(resultado2.isPresent());
        DepoimentoEntity depoimentoEncontrado2 = resultado2.get();
        assertEquals(idDepoimentoCriado2, depoimentoEncontrado2.getIdDepoimento());
        assertEquals("depoimento teste 2", depoimentoEncontrado2.getTexto());
    }
    @Test
    public void naoDeveSalvarDepoimentoSemEgresso(){
        // CENARIO
        DepoimentoEntity depoimentoEntitySemEgresso = DepoimentoEntity.builder()
                .texto("depoimento teste sem egresso")
                .data(LocalDate.now())
                .build();
        // ACAO e VALIDACAO
        assertThrows(ConstraintViolationException.class, () -> depoimentoJpaRepository.save(depoimentoEntitySemEgresso));
    }

    @Test
    @Transactional
    public void deveVerificarBuscarTodosDepoimentos(){
        // CENARIO
        DepoimentoEntity depoimento1 = DepoimentoEntity.builder()
                .texto("texto depoimento 1")
                .egresso(egressoBase)
                .data(LocalDate.now())
                .build();
        DepoimentoEntity depoimento2 = DepoimentoEntity.builder()
                .texto("texto depoimento 2")
                .egresso(egressoBase)
                .data(LocalDate.now())
                .build();
        DepoimentoEntity depoimento3 = DepoimentoEntity.builder()
                .texto("texto depoimento 3")
                .egresso(egressoBase)
                .data(LocalDate.now())
                .build();
        depoimentoJpaRepository.save(depoimento1);
        depoimentoJpaRepository.save(depoimento2);
        depoimentoJpaRepository.save(depoimento3);

        // ACAO
        List<DepoimentoEntity> resultado = depoimentoJpaRepository.findAll();
        // VALIDACAO
        assertFalse(resultado.isEmpty());
        assertEquals(3, resultado.size());
    }
}
