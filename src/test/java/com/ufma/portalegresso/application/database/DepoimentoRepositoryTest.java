package com.ufma.portalegresso.application.database;

import com.ufma.portalegresso.application.domain.Depoimento;
import com.ufma.portalegresso.application.domain.Egresso;
import com.ufma.portalegresso.application.out.DepoimentoJpaRepository;
import com.ufma.portalegresso.application.out.EgressoJpaRepository;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.AfterAll;
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
    public void deveVerificarSalvarDepoimento(){
        // CENARIO
        Depoimento depoimento = Depoimento.builder()
                .egresso(egressoBase)
                .texto("texto salvar depoimento teste")
                .data(LocalDate.now())
                .build();
        // ACAO
        Depoimento depoimentoSalvo = depoimentoJpaRepository.save(depoimento);
        // VERIFICACAO
        assertNotNull(depoimentoSalvo);
        assertEquals(depoimento.getEgresso(),depoimentoSalvo.getEgresso());
        assertEquals(depoimento.getTexto(),depoimentoSalvo.getTexto());
        assertEquals(depoimento.getData(),depoimentoSalvo.getData());
    }

    @Test
    @Transactional
    public void deveVerificarBuscarDepoimentoPorId(){
        // CENARIO
        Integer idDepoimentoCriado1 = depoimentoJpaRepository.save(Depoimento.builder()
                .egresso(egressoBase)
                .texto("depoimento teste 1")
                .data(LocalDate.now())
                .build()).getIdDepoimento();
        Integer idDepoimentoCriado2 = depoimentoJpaRepository.save(Depoimento.builder()
                .egresso(egressoBase)
                .texto("depoimento teste 2")
                .data(LocalDate.now())
                .build()).getIdDepoimento();
        // ACAO
        Optional<Depoimento> resultado1 = depoimentoJpaRepository.findById(idDepoimentoCriado1);
        Optional<Depoimento> resultado2 = depoimentoJpaRepository.findById(idDepoimentoCriado2);
        // VALIDAÇÃO
        assertTrue(resultado1.isPresent());
        Depoimento depoimentoEncontrado1 = resultado1.get();
        assertEquals(idDepoimentoCriado1, depoimentoEncontrado1.getIdDepoimento());
        assertEquals("depoimento teste 1", depoimentoEncontrado1.getTexto());

        assertTrue(resultado2.isPresent());
        Depoimento depoimentoEncontrado2 = resultado2.get();
        assertEquals(idDepoimentoCriado2, depoimentoEncontrado2.getIdDepoimento());
        assertEquals("depoimento teste 2", depoimentoEncontrado2.getTexto());
    }
    @Test
    public void naoDeveSalvarDepoimentoSemEgresso(){
        // CENARIO
        Depoimento depoimentoSemEgresso = Depoimento.builder()
                .texto("depoimento teste sem egresso")
                .data(LocalDate.now())
                .build();
        // ACAO e VALIDACAO
        assertThrows(ConstraintViolationException.class, () -> depoimentoJpaRepository.save(depoimentoSemEgresso));
    }

    @Test
    @Transactional
    public void deveVerificarBuscarTodosDepoimentos(){
        // CENARIO
        Depoimento depoimento1 = Depoimento.builder()
                .texto("texto depoimento 1")
                .egresso(egressoBase)
                .data(LocalDate.now())
                .build();
        Depoimento depoimento2 = Depoimento.builder()
                .texto("texto depoimento 2")
                .egresso(egressoBase)
                .data(LocalDate.now())
                .build();
        Depoimento depoimento3 = Depoimento.builder()
                .texto("texto depoimento 3")
                .egresso(egressoBase)
                .data(LocalDate.now())
                .build();
        depoimentoJpaRepository.save(depoimento1);
        depoimentoJpaRepository.save(depoimento2);
        depoimentoJpaRepository.save(depoimento3);

        // ACAO
        List<Depoimento> resultado = depoimentoJpaRepository.findAll();
        // VALIDACAO
        assertFalse(resultado.isEmpty());
        assertEquals(3, resultado.size());
    }
}
