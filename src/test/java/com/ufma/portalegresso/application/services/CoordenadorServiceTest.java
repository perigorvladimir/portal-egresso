package com.ufma.portalegresso.application.services;

import com.ufma.portalegresso.application.domain.Coordenador;
import com.ufma.portalegresso.application.usecases.coordenador.SalvarCoordenadorUC;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class CoordenadorServiceTest {
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private CoordenadorService service;

    @Test
    @Transactional
    public void deveSalvarCoordenadorFluxoPadrao(){
        SalvarCoordenadorUC.Request request = SalvarCoordenadorUC.Request.builder()
                .nome("nome coordenador")
                .login("login")
                .senha("senhaOitoDigitos")
                .build();
        Coordenador coordenadorSalvo = service.salvarCoordenador(request, "noop");

        entityManager.clear();
        Coordenador coordenador = entityManager.find(Coordenador.class, coordenadorSalvo.getIdCoordenador());
        assertNotNull(coordenadorSalvo);
        assertNotNull(coordenadorSalvo.getIdCoordenador());
        assertEquals("ROLE_ADMIN", coordenador.getRole());
        assertEquals(request.getNome(), coordenadorSalvo.getNome());
        assertEquals(request.getLogin(), coordenadorSalvo.getLogin());
        assertEquals(request.getSenha(), coordenadorSalvo.getSenha().split("}")[1]);
    }

    @Test
    public void deveGerarErroAoSalvarCoordenadorSemNomeOuLoginOuSenha(){

    }

    @Test
    public void deveGerarErroAoBuscarCoordenadorPorIdNull(){
        assertThrows(IllegalArgumentException.class, () -> service.buscarCoordenadorPorId(null));
    }
}
