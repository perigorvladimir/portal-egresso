package com.ufma.portalegressos.database;

import com.ufma.portalegressos.application.domain.Egresso;
import com.ufma.portalegressos.application.out.EgressoJpaRepository;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;


import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class EgressoRepositoryTest {
    private static final Logger logger = LoggerFactory.getLogger(EgressoRepositoryTest.class);
    @Autowired
    private EgressoJpaRepository egressoJpaRepository;
    @Test
    @Transactional
    public void deveVerificarSalvarEgresso(){
        // CENARIO
        Egresso egresso = Egresso.builder()
                .nome("Igor Vladimir Cunha de Alencar")
                .email("igor.vladimir@discente.ufma.br")
                .curriculo("curriculo teste curriculo teste curriculo teste curriculo teste curriculo teste")
                .descricao("graduando CP")
                .linkedin("https://www.linkedin.com/in/igorvalencar/")
                .Instagram("instagram_teste").build();

        // AÇÃO
        Egresso egressoSalvo = egressoJpaRepository.save(egresso);

        // VERIFICAÇÃO
        assertNotNull(egressoSalvo);
        assertEquals(egresso.getNome(), egressoSalvo.getNome());
        assertEquals(egresso.getEmail(), egressoSalvo.getEmail());
        assertEquals(egresso.getCurriculo(), egressoSalvo.getCurriculo());
        assertEquals(egresso.getDescricao(), egressoSalvo.getDescricao());
        assertEquals(egresso.getLinkedin(), egressoSalvo.getLinkedin());
        assertEquals(egresso.getInstagram(), egressoSalvo.getInstagram());
    }
    @Test
    @Transactional
    public void deveVerificarBuscarEgressoPorId(){
        // CENARIO
        Integer idEgresso = egressoJpaRepository.save(Egresso.builder()
                                                .nome("Igor Vladimir Cunha de Alencar")
                                                .email("igor.vladimir@discente.ufma.br")
                                                .curriculo("curriculo teste curriculo teste curriculo teste curriculo teste curriculo teste")
                                                .descricao("graduando CP")
                                                .linkedin("https://www.linkedin.com/in/igorvalencar/")
                                                .Instagram("instagram_teste").build()).getIdEgresso();
        // AÇÃO
        Optional<Egresso> resultado = egressoJpaRepository.findById(idEgresso);

        // VERIFICACAO
        assertTrue(resultado.isPresent());
        Egresso egressoEncontrado = resultado.get();
        assertEquals(idEgresso, egressoEncontrado.getIdEgresso());
        assertEquals("Igor Vladimir Cunha de Alencar", egressoEncontrado.getNome());
    }
    @Test
    public void naoDeveSalvarEgressoSemNomeOuEmail(){
        // CENARIO
        Egresso egresso = Egresso.builder()
                .email("igor.vladimir@discente.ufma.br")
                .curriculo("curriculo teste curriculo teste")
                .descricao("1223")
                .build();
        // ACAO e VALIDACAO
        assertThrows(ConstraintViolationException.class, () -> egressoJpaRepository.save(egresso));
        egresso.setNome("Igor Vladimir");
        egresso.setEmail(null);
        assertThrows(ConstraintViolationException.class, () -> egressoJpaRepository.save(egresso));
    }

    @Test
    @Transactional
    public void deveVerificarBuscarTodosEgressos(){
        // CENARIO
        Egresso egresso1 = Egresso.builder()
                .nome("Igor Vladimir Cunha de Alencar")
                .email("igor.vladimir@discente.ufma.br")
                .curriculo("curriculo teste curriculo teste curriculo teste curriculo teste curriculo teste")
                .descricao("graduando CP")
                .linkedin("https://www.linkedin.com/in/igorvalencar/")
                .Instagram("instagram_teste").build();
        Egresso egresso2 = Egresso.builder()
                .nome("Fulano da Silva")
                .email("fulano.silva@discente.ufma.br")
                .curriculo("curriculo de Fulano da Silva")
                .descricao("descrição do FUlano da Silva")
                .linkedin("https://www.linkedin.com/in/fulanoDaSilva/")
                .Instagram("fulano_silva").build();
        Egresso egresso3 = Egresso.builder()
                .nome("Fulano da Silva")
                .email("fulano.silva@discente.ufma.br")
                .curriculo("curriculo de Fulano da Silva")
                .descricao("descrição do FUlano da Silva")
                .linkedin("https://www.linkedin.com/in/fulanoDaSilva/")
                .Instagram("fulano_silva").build();
        egressoJpaRepository.save(egresso1);
        egressoJpaRepository.save(egresso2);
        egressoJpaRepository.save(egresso3);

        // ACAO
        List<Egresso> resultado = egressoJpaRepository.findAll();
        // VALIDACAO
        assertFalse(resultado.isEmpty());
        assertEquals(3, resultado.size());

    }

}
