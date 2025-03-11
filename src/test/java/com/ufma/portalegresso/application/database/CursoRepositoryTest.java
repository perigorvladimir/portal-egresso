package com.ufma.portalegresso.application.database;

import com.ufma.portalegresso.application.domain.*;
import com.ufma.portalegresso.application.domain.relacionamentos.CursoEgressoId;
import com.ufma.portalegresso.application.out.CoordenadorJpaRepository;
import com.ufma.portalegresso.application.out.CursoEgressoJpaRepository;
import com.ufma.portalegresso.application.out.CursoJpaRepository;
import com.ufma.portalegresso.application.out.EgressoJpaRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class CursoRepositoryTest {
    @Autowired
    private CursoJpaRepository cursoJpaRepository;
    private static Coordenador coordenadorBase;
    @Autowired
    private EgressoJpaRepository egressoJpaRepository;
    @Autowired
    private CursoEgressoJpaRepository cursoEgressoJpaRepository;

    @BeforeAll
    public static void setUp(@Autowired CoordenadorJpaRepository coordenadorJpaRepository) {
        Coordenador coordenador = Coordenador.builder()
                .login("login")
                .senha("senha")
                .nome("Igor")
                .build();
        coordenadorBase = coordenadorJpaRepository.save(coordenador);
    }
    @AfterAll
    public static void cleanUp(@Autowired CoordenadorJpaRepository coordenadorJpaRepository) {
        coordenadorJpaRepository.deleteById(coordenadorBase.getIdCoordenador());
    }
    @Test
    @Transactional
    public void deveSalvarCoordenadorFluxoPadrao() {
        Curso curso = Curso.builder()
                .nome("Ciência da Computação")
                .tipoNivel(TipoNivel.GRADUACAO)
                .coordenador(coordenadorBase)
                .build();

        Curso cursoSalvo = cursoJpaRepository.save(curso);

        assertNotNull(cursoSalvo.getIdCurso());
        assertEquals(curso.getNome(), cursoSalvo.getNome());
        assertEquals(curso.getTipoNivel(), cursoSalvo.getTipoNivel());
        assertEquals(curso.getCoordenador(), cursoSalvo.getCoordenador());
    }

    @Test
    @Transactional
    public void deveVerificarBuscarCursoPorId() {
        Curso curso1 = cursoJpaRepository.save(Curso.builder()
                .nome("Ciência da Computação")
                .tipoNivel(TipoNivel.GRADUACAO)
                .coordenador(null)
                .build());
        Curso curso2 = cursoJpaRepository.save(Curso.builder()
                .nome("Engenharia de Software")
                .tipoNivel(TipoNivel.GRADUACAO)
                .coordenador(coordenadorBase)
                .build());

        Optional<Curso> resposta1 = cursoJpaRepository.findById(curso1.getIdCurso());
        Optional<Curso> resposta2 = cursoJpaRepository.findById(curso2.getIdCurso());

        // resultado1
        assertTrue(resposta1.isPresent());
        Curso cursoEncontrado1 = resposta1.get();
        assertEquals(curso1.getIdCurso(), cursoEncontrado1.getIdCurso());
        assertEquals(curso1.getNome(), cursoEncontrado1.getNome());
        assertEquals(curso1.getTipoNivel(), cursoEncontrado1.getTipoNivel());
        assertEquals(curso1.getCoordenador(), cursoEncontrado1.getCoordenador());

        // resultado2
        assertTrue(resposta2.isPresent());
        Curso cursoEncontrado2 = resposta2.get();
        assertEquals(curso2.getIdCurso(), cursoEncontrado2.getIdCurso());
        assertEquals(curso2.getNome(), cursoEncontrado2.getNome());
        assertEquals(curso2.getTipoNivel(), cursoEncontrado2.getTipoNivel());
        assertEquals(curso2.getCoordenador(), cursoEncontrado2.getCoordenador());
    }

    @Test
    public void naoDeveSalvarCursoSemNomeOuNivel() {
        Curso curso = Curso.builder()
                .nome("Ciência da Computação")
                .coordenador(coordenadorBase)
                .build();
        DataIntegrityViolationException exceptionSemNivel = assertThrows(DataIntegrityViolationException.class, () -> cursoJpaRepository.save(curso));
        assertEquals("nivel", exceptionSemNivel.getCause().getCause().getLocalizedMessage().split("\"")[1].toLowerCase());

        curso.setNome(null);
        curso.setTipoNivel(TipoNivel.GRADUACAO);
        DataIntegrityViolationException exceptionSemNome = assertThrows(DataIntegrityViolationException.class, () -> cursoJpaRepository.save(curso));
        assertEquals("nome", exceptionSemNome.getCause().getCause().getLocalizedMessage().split("\"")[1].toLowerCase());

        curso.setNome("Ciência da Computação");
        Curso cursoSalvo = assertDoesNotThrow(() -> cursoJpaRepository.save(curso));
        cursoJpaRepository.deleteById(cursoSalvo.getIdCurso());
    }

    @Test
    @Transactional
    public void deveVerificarBuscarTodosCursos(){
        Curso curso1 = cursoJpaRepository.save(Curso.builder()
                .nome("curso nome 1")
                .tipoNivel(TipoNivel.GRADUACAO)
                .coordenador(coordenadorBase)
                .build());
        Curso curso2 = cursoJpaRepository.save(Curso.builder()
                .nome("curso nome 2")
                .tipoNivel(TipoNivel.GRADUACAO)
                .build());
        Curso curso3 = cursoJpaRepository.save(Curso.builder()
                .nome("Design de Móveis")
                .tipoNivel(TipoNivel.TECNOLOGO)
                .coordenador(coordenadorBase)
                .build());
        cursoJpaRepository.save(curso1);
        cursoJpaRepository.save(curso2);
        cursoJpaRepository.save(curso3);

        List<Curso> cursos = cursoJpaRepository.findAll();

        assertFalse(cursos.isEmpty());
        assertEquals(3, cursos.size());
        assertTrue(cursos.contains(curso1));
        assertTrue(cursos.contains(curso2));
        assertTrue(cursos.contains(curso3));
    }

    //sem atualizar pois nao faz sentido para o sistema

    @Test
    @Transactional
    public void deveDeletarCursoPorId(){
        Curso cursoSalvo = cursoJpaRepository.save(Curso.builder()
                .nome("Design de Móveis")
                .tipoNivel(TipoNivel.TECNOLOGO)
                .coordenador(coordenadorBase)
                .build());
        cursoJpaRepository.deleteById(cursoSalvo.getIdCurso());

        assertThrows(NoSuchElementException.class, () -> cursoJpaRepository.findById(cursoSalvo.getIdCurso()).get());
    }
    @Test
    @Transactional
    public void deveCursoPoderTerVariosEgressos(){
        Curso cursoSalvo = cursoJpaRepository.save(Curso.builder()
                .nome("Design de Móveis")
                .tipoNivel(TipoNivel.TECNOLOGO)
                .coordenador(coordenadorBase)
                .build());
        Egresso egresso = egressoJpaRepository.save(Egresso.builder().email("igor@exemplo.com").nome("igor").build());
        Egresso egresso2 = egressoJpaRepository.save(Egresso.builder().email("geraldo@exemplo.com").nome("geraldo").build());

        CursoEgressoId id = CursoEgressoId.builder().idCurso(cursoSalvo.getIdCurso()).idEgresso(egresso.getIdEgresso()).build();
        CursoEgresso cursoEgressoSalvo = cursoEgressoJpaRepository.save(CursoEgresso.builder()
                .id(id)
                .egresso(egresso)
                .curso(cursoSalvo)
                .anoInicio(2020)
                .anoFim(2025)
                .build());
        CursoEgressoId id2 = CursoEgressoId.builder().idCurso(cursoSalvo.getIdCurso()).idEgresso(egresso2.getIdEgresso()).build();
        CursoEgresso cursoEgressoSalvo2 = cursoEgressoJpaRepository.save(CursoEgresso.builder()
                .id(id2)
                .egresso(egresso2)
                .curso(cursoSalvo)
                .anoInicio(2020)
                .anoFim(null)
                .build());

        assertEquals(cursoSalvo.getIdCurso(), cursoEgressoSalvo.getId().getIdCurso());
        assertEquals(cursoSalvo.getIdCurso(), cursoEgressoSalvo2.getId().getIdCurso());
    }
}
