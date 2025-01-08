package com.ufma.portalegressos.services;

import com.ufma.portalegressos.application.domain.Coordenador;
import com.ufma.portalegressos.application.services.CoordenadorService;
import com.ufma.portalegressos.application.usecases.coordenador.SalvarCoordenadorUC;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Profile("test")
public class CoordenadorServiceTest {
    @Autowired
    private CoordenadorService service;

    @Test
    @Transactional
    public void deveSalvarCoordenadorPadrao(){
        SalvarCoordenadorUC.Request request = SalvarCoordenadorUC.Request.builder()
                .nome("nome coordenador")
                .login("login")
                .senha("senha")
                .build();
        Coordenador coordenadorSalvo = service.salvarCoordenador(request, null);

        assertNotNull(coordenadorSalvo);
        assertNotNull(coordenadorSalvo.getIdCoordenador());
        assertEquals("ROLE_COORDENADOR", coordenadorSalvo.getRole());
        assertEquals(request.getNome(), coordenadorSalvo.getNome());
        assertEquals(request.getLogin(), coordenadorSalvo.getLogin());
        assertEquals(request.getSenha(), coordenadorSalvo.getSenha());
    }

    @Test
    public void deveGerarErroAoSalvarCoordenadorSemNomeOuLoginOuSenha(){
        SalvarCoordenadorUC.Request request = SalvarCoordenadorUC.Request.builder()
                .login("login")
                .senha("senha")
                .build();
        assertThrows(ConstraintViolationException.class, () -> service.salvarCoordenador(request, null));

        SalvarCoordenadorUC.Request request2 = SalvarCoordenadorUC.Request.builder()
                .nome("nome coordenador")
                .senha("senha")
                .build();
        assertThrows(ConstraintViolationException.class, () -> service.salvarCoordenador(request2, null));

        SalvarCoordenadorUC.Request request3 = SalvarCoordenadorUC.Request.builder()
                .login("login")
                .nome("nome coordenador")
                .build();
        assertThrows(ConstraintViolationException.class, () -> service.salvarCoordenador(request3, null));
    }
}
