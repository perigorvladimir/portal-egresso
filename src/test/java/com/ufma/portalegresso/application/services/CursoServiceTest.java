package com.ufma.portalegresso.application.services;

import com.ufma.portalegresso.application.domain.Coordenador;
import com.ufma.portalegresso.application.domain.Curso;
import com.ufma.portalegresso.application.out.CoordenadorJpaRepository;
import com.ufma.portalegresso.application.out.CursoJpaRepository;
import com.ufma.portalegresso.application.usecases.curso.CursoUC;
import com.ufma.portalegresso.application.usecases.curso.SalvarCursoUC;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class CursoServiceTest {
    @Autowired
    private CursoService service;

    private static Coordenador coordenadorBase;
    @Autowired
    private CursoJpaRepository cursoJpaRepository;
    @Autowired
    private CursoUC cursoUC;

    @BeforeAll
    public static void setUp(@Autowired CoordenadorJpaRepository coordenadorJpaRepository){
        Coordenador coordenador = Coordenador.builder()
                .nome("coordenador base")
                .login("login")
                .senha("senha")
                .build();

        coordenadorBase = coordenadorJpaRepository.save(coordenador);
    }
    @AfterAll
    public static void cleanUp(@Autowired CoordenadorJpaRepository coordenadorJpaRepository){
        coordenadorJpaRepository.deleteById(coordenadorBase.getIdCoordenador());
    }

    @Test
    @Transactional
    public void deveSalvarCursoPadrao(){
        SalvarCursoUC.Request curso = SalvarCursoUC.Request.builder()
                .nome("Ciência da Computação")
                .tipoNivel("GRADUACAO")
                .idCoordenador(coordenadorBase.getIdCoordenador())
                .build();

        Curso cursoSalvo = service.salvarCurso(curso);

        assertNotNull(cursoSalvo);
        assertNotNull(cursoSalvo.getIdCurso());
    }
    @Test
    @Transactional
    public void deveBuscarCursoPorIdPadrao(){
        Curso cursoCriado = service.salvarCurso(SalvarCursoUC.Request.builder()
                .nome("Ciência da Computação")
                .tipoNivel("GRADUACAO")
                .idCoordenador(coordenadorBase.getIdCoordenador())
                .build());

        Curso cursoEncontrado = service.buscarPorId(cursoCriado.getIdCurso());

        assertEquals(cursoCriado.getIdCurso(), cursoEncontrado.getIdCurso());
        assertEquals(cursoCriado.getCoordenador().getIdCoordenador(), cursoEncontrado.getCoordenador().getIdCoordenador());
    }
    @Test
    @Transactional
    public void deveBuscarTodosCursosPadrao(){
        Curso cursoCriado1 = service.salvarCurso(SalvarCursoUC.Request.builder()
                .nome("Ciência da Computação")
                .tipoNivel("TECNOLOGO")
                .idCoordenador(coordenadorBase.getIdCoordenador())
                .build());
        Curso cursoCriado2 = service.salvarCurso(SalvarCursoUC.Request.builder()
                .nome("Economia")
                .tipoNivel("DOUTORADO")
                .idCoordenador(coordenadorBase.getIdCoordenador())
                .build());
        Curso cursoCriado3 = service.salvarCurso(SalvarCursoUC.Request.builder()
                .nome("Filosofia")
                .tipoNivel("MESTRADO")
                .idCoordenador(coordenadorBase.getIdCoordenador())
                .build());
        List<Curso> cursos = service.buscarTodosCursos();
        assertFalse(cursos.isEmpty());
        assertEquals(3, cursos.size());
        assertTrue(cursos.contains(cursoCriado1));
        assertTrue(cursos.contains(cursoCriado2));
        assertTrue(cursos.contains(cursoCriado3));
    }
    @Test
    @Transactional
    public void deveDeletarCursoPadrao(){
        Curso cursoCriado = service.salvarCurso(SalvarCursoUC.Request
                .builder()
                .nome("Curso deletar padrao")
                .tipoNivel("GRADUACAO")
                .idCoordenador(coordenadorBase.getIdCoordenador())
                .build());

        service.deletarCursoPorId(cursoCriado.getIdCurso());

        Optional<Curso> cursoEncontrado = cursoJpaRepository.findById(cursoCriado.getIdCurso());
        assertTrue(cursoEncontrado.isEmpty());
    }
}
