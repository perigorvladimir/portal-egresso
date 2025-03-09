package com.ufma.portalegresso.application.services;

import com.ufma.portalegresso.application.domain.Coordenador;
import com.ufma.portalegresso.application.domain.Curso;
import com.ufma.portalegresso.application.out.CoordenadorJpaRepository;
import com.ufma.portalegresso.application.usecases.coordenador.SalvarCoordenadorUC;
import com.ufma.portalegresso.application.usecases.curso.*;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class CursoServiceTest {
    @Autowired
    private CursoService service;

    private static Coordenador coordenadorBase;
    private static Coordenador coordenadorBase2;

    @BeforeAll
    public static void setUp(@Autowired CoordenadorService coordenadorService){
        SalvarCoordenadorUC.Request coordenador = SalvarCoordenadorUC.Request.builder()
                .nome("coordenador base")
                .login("login")
                .senha("senha123")
                .build();
        SalvarCoordenadorUC.Request coordenador2 = SalvarCoordenadorUC.Request.builder()
                .nome("coordenador 2")
                .login("login2")
                .senha("senha231")
                .build();

        coordenadorBase = coordenadorService.salvarCoordenador(coordenador, null);
        coordenadorBase2 = coordenadorService.salvarCoordenador(coordenador2, null);
    }
    @AfterAll
    public static void cleanUp(@Autowired CoordenadorService coordenadorService){
        coordenadorService.deletarCoordenadorPorId(coordenadorBase.getIdCoordenador());
        coordenadorService.deletarCoordenadorPorId(coordenadorBase2.getIdCoordenador());
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
    public void deveGerarErroAoSalvarCursoComCoordenadorInexistente(){
        SalvarCursoUC.Request curso = SalvarCursoUC.Request.builder()
                .nome("Ciência da Computação")
                .tipoNivel("GRADUACAO")
                .idCoordenador(100)
                .build();

        assertThrows(EntityNotFoundException.class, () -> service.salvarCurso(curso));
    }
    @Test
    public void deveGerarErroAoSalvarCursoComTipoNivelInexistenteOuNull(){
        SalvarCursoUC.Request cursoTipoNivelNull = SalvarCursoUC.Request.builder()
                .nome("Ciência da Computação")
                .idCoordenador(coordenadorBase.getIdCoordenador())
                .build();

        SalvarCursoUC.Request cursoTipoNivelInexistente = SalvarCursoUC.Request.builder()
                .nome("Ciência da Computação")
                .tipoNivel("inexistente")
                .idCoordenador(coordenadorBase.getIdCoordenador())
                .build();

        assertThrows(DataIntegrityViolationException.class, () -> service.salvarCurso(cursoTipoNivelNull));
        assertThrows(IllegalArgumentException.class, () -> service.salvarCurso(cursoTipoNivelInexistente));
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
    public void deveBuscarCursoPorIdPadrao(){
        Curso cursoCriado = criarCursoTestes();

        Curso cursoEncontrado = service.buscarPorId(cursoCriado.getIdCurso());

        assertEquals(cursoCriado.getIdCurso(), cursoEncontrado.getIdCurso());
        assertEquals(cursoCriado.getNome(), cursoEncontrado.getNome());
        assertEquals(cursoCriado.getTipoNivel(), cursoEncontrado.getTipoNivel());
        assertEquals(cursoCriado.getCoordenador().getIdCoordenador(), cursoEncontrado.getCoordenador().getIdCoordenador());
    }
    @Test
    @Transactional
    public void deveGerarErroAoBuscarCursoPorIdInexistente(){
        criarCursoTestes();

        assertThrows(EntityNotFoundException.class, () -> service.buscarPorId(100));
    }
    @Test
    @Transactional
    public void deveGerarErroAoBuscarCursoPorIdNull(){
        criarCursoTestes();

        assertThrows(IllegalArgumentException.class, () -> service.buscarPorId(null));
    }
    @Test
    @Transactional
    public void deveAtualizarCursoFluxoPadrao(){
        Curso cursoCriado = criarCursoTestes();

        UpdateCursoUc.Request cursoAtualizado = UpdateCursoUc.Request.builder()
                .nome("Ciência da Computação")
                .build();

        service.update(cursoCriado.getIdCurso(), cursoAtualizado);

        Curso cursoEncontrado = service.buscarPorId(cursoCriado.getIdCurso());

        assertEquals(cursoCriado.getIdCurso(), cursoEncontrado.getIdCurso());
        assertEquals(cursoCriado.getNome(), cursoEncontrado.getNome());
        assertEquals(cursoCriado.getTipoNivel(), cursoEncontrado.getTipoNivel());
        assertEquals(cursoCriado.getCoordenador().getIdCoordenador(), cursoEncontrado.getCoordenador().getIdCoordenador());
    }
    @Test
    @Transactional
    public void deveGerarErroAoAtualizarCursoComIdInexistente(){
        assertThrows(EntityNotFoundException.class, () -> service.update(100, UpdateCursoUc.Request.builder().build()));
    }
    @Test
    @Transactional
    public void deveGerarErroAoAtualizarCursoComIdNull(){
        assertThrows(IllegalArgumentException.class, () -> service.update(null, UpdateCursoUc.Request.builder().build()));
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

        assertThrows(EntityNotFoundException.class, () -> service.buscarPorId(cursoCriado.getIdCurso()));
    }
    @Test
    @Transactional
    public void deveDesignarCoordenadorFluxoPadrao(){
        Curso cursoCriado = criarCursoTestes();

        DesignarCoordenadorUC.Response response = service.designarCoordenador(cursoCriado.getIdCurso(), coordenadorBase.getIdCoordenador());

        assertEquals(response.getIdNovoCoord(), coordenadorBase.getIdCoordenador());
        assertEquals(response.getNomeNovoCoord(), coordenadorBase.getNome());
        assertEquals(response.getIdCurso(), cursoCriado.getIdCurso());
        assertEquals(response.getNomeCurso(), cursoCriado.getNome());
    }
    @Test
    public void deveGerarErroAoDesignarCoordenadorComIdCursoNull(){
        assertThrows(IllegalArgumentException.class, () -> service.designarCoordenador(null, coordenadorBase.getIdCoordenador()));
    }
    @Test
    public void deveGerarErroAoDesignarCoordenadorComIdCursoInexistente(){
        assertThrows(EntityNotFoundException.class, () -> service.designarCoordenador(100, coordenadorBase.getIdCoordenador()));
    }
    @Test
    @Transactional
    public void deveGerarErroAoDesignarCoordenadorComIdCoordenadorNull(){
        Curso cursoCriado = criarCursoTestes();
        assertThrows(IllegalArgumentException.class, () -> service.designarCoordenador(cursoCriado.getIdCurso(), null));
    }
    @Test
    @Transactional
    public void deveGerarErroAoDesignarCoordenadorComIdCoordenadorInexistente(){
        Curso cursoCriado = criarCursoTestes();
        assertThrows(EntityNotFoundException.class, () -> service.designarCoordenador(cursoCriado.getIdCurso(), 100));
    }
    @Test
    @Transactional
    public void deveDesignarCoordenadorSubstituindoAntigo(){
        Curso cursoCriado = criarCursoTestes();

        service.designarCoordenador(cursoCriado.getIdCurso(), coordenadorBase.getIdCoordenador());

        service.designarCoordenador(cursoCriado.getIdCurso(), coordenadorBase2.getIdCoordenador());

        assertEquals(cursoCriado.getCoordenador().getIdCoordenador(), coordenadorBase2.getIdCoordenador());
        assertEquals(cursoCriado.getCoordenador().getNome(), coordenadorBase2.getNome());
    }

    private Curso criarCursoTestes(){
        Curso cursoCriado = service.salvarCurso(SalvarCursoUC.Request.builder()
                .nome("Ciência da Computação")
                .tipoNivel("GRADUACAO")
                .idCoordenador(coordenadorBase.getIdCoordenador())
                .build());
        return cursoCriado;
    }
}
