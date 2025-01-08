package com.ufma.portalegressos.services;

import com.ufma.portalegressos.application.domain.Curso;
import com.ufma.portalegressos.application.services.CursoService;
import com.ufma.portalegressos.application.usecases.curso.SalvarCursoUC;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Profile("test")
public class CursoServiceTest {
    @Autowired
    private CursoService service;

    //TODO FAZER SETUP DE UM COORDENADOR

    @Test
    public void deveSalvarCursoPadrao(){
        SalvarCursoUC.Request curso = SalvarCursoUC.Request.builder()
                .nome("Ciência da Computação")
                .nivel("Graduação")
                .build();

        Curso cursoSalvo = service.salvarCurso(curso);

        assertNotNull(cursoSalvo);
        assertNotNull(cursoSalvo.getIdCurso());
    }
    @Test
    public void deveBuscarCursoPorIdPadrao(){
        Curso cursoCriado = service.salvarCurso(SalvarCursoUC.Request.builder()
                .nome("Ciência da Computação")
                .nivel("Graduação")
                .build());

        Curso cursoEncontrado = service.buscarPorId(cursoCriado.getIdCurso());

        assertEquals(cursoCriado.getIdCurso(), cursoEncontrado.getIdCurso());
        assertEquals(cursoCriado.getCoordenador(), cursoEncontrado.getCoordenador());
    }

}
