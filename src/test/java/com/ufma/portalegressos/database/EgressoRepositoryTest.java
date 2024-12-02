package com.ufma.portalegressos.database;

import com.ufma.portalegressos.database.entities.EgressoEntity;
import com.ufma.portalegressos.database.repositories.EgressoJpaRepository;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import jakarta.transaction.Transactional;

import java.util.Optional;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class EgressoRepositoryTest {
    @Autowired
    private EgressoJpaRepository egressoJpaRepository;
    @Test
    public void deveVerificarSalvarEgresso(){
        // CENARIO
        EgressoEntity egresso = EgressoEntity.builder()
                .nome("Igor Vladimir Cunha de Alencar")
                .email("igor.vladimir@discente.ufma.br")
                .curriculo("curriculo teste curriculo teste curriculo teste curriculo teste curriculo teste")
                .descricao("graduando CP")
                .linkedin("https://www.linkedin.com/in/igorvalencar/")
                .Instagram("instagram_teste").build();

        // AÇÃO
        EgressoEntity egressoSalvo = egressoJpaRepository.save(egresso);

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
    public void deveVerificarBuscarEgressoPorId(){
        // CENARIO
        Integer idEgresso = egressoJpaRepository.save(EgressoEntity.builder()
                                                .nome("Igor Vladimir Cunha de Alencar")
                                                .email("igor.vladimir@discente.ufma.br")
                                                .curriculo("curriculo teste curriculo teste curriculo teste curriculo teste curriculo teste")
                                                .descricao("graduando CP")
                                                .linkedin("https://www.linkedin.com/in/igorvalencar/")
                                                .Instagram("instagram_teste").build()).getIdEgresso();
        // AÇÃO
        Optional<EgressoEntity> resultado = egressoJpaRepository.findById(idEgresso);

        // VERIFICACAO
        assertTrue(resultado.isPresent());
        EgressoEntity egressoEncontrado = resultado.get();
        assertEquals(1, egressoEncontrado.getIdEgresso());
        assertEquals("Igor Vladimir Cunha de Alencar", egressoEncontrado.getNome());
    }
}
