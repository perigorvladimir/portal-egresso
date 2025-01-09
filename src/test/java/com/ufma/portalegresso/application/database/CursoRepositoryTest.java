package com.ufma.portalegresso.application.database;

import com.ufma.portalegresso.application.domain.Coordenador;
import com.ufma.portalegresso.application.domain.Curso;
import com.ufma.portalegresso.application.out.CoordenadorJpaRepository;
import com.ufma.portalegresso.application.out.CursoJpaRepository;
import jakarta.validation.ConstraintViolationException;
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
public class CursoRepositoryTest {
    @Autowired
    private CursoJpaRepository cursoJpaRepository;
    private static Coordenador coordenadorBase;
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
    public void deveVerificarFluxoPrincipalSalvar() {
        Curso curso = Curso.builder()
                .nome("Ciência da Computação")
                .nivel("Graduação")
                .coordenador(coordenadorBase)
                .build();

        Curso cursoSalvo = cursoJpaRepository.save(curso);

        assertNotNull(cursoSalvo.getIdCurso());
        assertEquals(curso.getNome(), cursoSalvo.getNome());
        assertEquals(curso.getNivel(), cursoSalvo.getNivel());
        assertEquals(curso.getCoordenador(), cursoSalvo.getCoordenador());
    }

    @Test
    @Transactional
    public void deveVerificarBuscarCursoPorId() {
        Curso curso1 = cursoJpaRepository.save(Curso.builder()
                .nome("Ciência da Computação")
                .nivel("Graduação")
                .coordenador(null)
                .build());
        Curso curso2 = cursoJpaRepository.save(Curso.builder()
                .nome("Engenharia de Software")
                .nivel("Graduação")
                .coordenador(coordenadorBase)
                .build());

        Optional<Curso> resposta1 = cursoJpaRepository.findById(curso1.getIdCurso());
        Optional<Curso> resposta2 = cursoJpaRepository.findById(curso2.getIdCurso());

        // resultado1
        assertTrue(resposta1.isPresent());
        Curso cursoEncontrado1 = resposta1.get();
        assertEquals(curso1.getIdCurso(), cursoEncontrado1.getIdCurso());
        assertEquals(curso1.getNome(), cursoEncontrado1.getNome());
        assertEquals(curso1.getNivel(), cursoEncontrado1.getNivel());
        assertEquals(curso1.getCoordenador(), cursoEncontrado1.getCoordenador());

        // resultado2
        assertTrue(resposta2.isPresent());
        Curso cursoEncontrado2 = resposta2.get();
        assertEquals(curso2.getIdCurso(), cursoEncontrado2.getIdCurso());
        assertEquals(curso2.getNome(), cursoEncontrado2.getNome());
        assertEquals(curso2.getNivel(), cursoEncontrado2.getNivel());
        assertEquals(curso2.getCoordenador(), cursoEncontrado2.getCoordenador());
    }

    @Test
    public void naoDeveSalvarCursoSemNomeOuNivel() {
        Curso curso = Curso.builder()
                .nome("Ciência da Computação")
                .coordenador(coordenadorBase)
                .build();
        assertThrows(ConstraintViolationException.class, () -> cursoJpaRepository.save(curso));
        curso.setNome(null);
        curso.setNivel("Graduação");
        assertThrows(ConstraintViolationException.class, () -> cursoJpaRepository.save(curso));
    }

    @Test
    @Transactional
    public void deveVerificarBuscarTodosCursos(){
        Curso curso1 = cursoJpaRepository.save(Curso.builder()
                .nome("curso nome 1")
                .nivel("Graduação")
                .coordenador(coordenadorBase)
                .build());
        Curso curso2 = cursoJpaRepository.save(Curso.builder()
                .nome("curso nome 2")
                .nivel("Graduação")
                .build());
        Curso curso3 = cursoJpaRepository.save(Curso.builder()
                .nome("Design de Móveis")
                .nivel("Técnico")
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
}
