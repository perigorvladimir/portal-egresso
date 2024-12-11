package com.ufma.portalegressos.database;

import com.ufma.portalegressos.infrastructure.entities.CoordenadorEntity;
import com.ufma.portalegressos.infrastructure.entities.CursoEntity;
import com.ufma.portalegressos.infrastructure.repositories.CoordenadorJpaRepository;
import com.ufma.portalegressos.infrastructure.repositories.CursoJpaRepository;
import jakarta.validation.ConstraintViolationException;
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
    private static CoordenadorEntity coordenadorBase;
    @BeforeAll
    public static void setUp(@Autowired CoordenadorJpaRepository coordenadorJpaRepository) {
        CoordenadorEntity coordenador = CoordenadorEntity.builder()
                .login("login")
                .senha("senha")
                .tipo("coordenador")
                .build();
        coordenadorBase = coordenadorJpaRepository.save(coordenador);
    }
    @Test
    @Transactional
    public void deveVerificarFluxoPrincipalSalvar() {
        CursoEntity curso = CursoEntity.builder()
                .nome("Ciência da Computação")
                .nivel("Graduação")
                .coordenador(coordenadorBase)
                .build();

        CursoEntity cursoSalvo = cursoJpaRepository.save(curso);

        assertNotNull(cursoSalvo.getIdCurso());
        assertEquals(curso.getNome(), cursoSalvo.getNome());
        assertEquals(curso.getNivel(), cursoSalvo.getNivel());
        assertEquals(curso.getCoordenador(), cursoSalvo.getCoordenador());
    }

    @Test
    @Transactional
    public void deveVerificarBuscarCursoPorId() {
        CursoEntity curso1 = cursoJpaRepository.save(CursoEntity.builder()
                .nome("Ciência da Computação")
                .nivel("Graduação")
                .coordenador(null)
                .build());
        CursoEntity curso2 = cursoJpaRepository.save(CursoEntity.builder()
                .nome("Engenharia de Software")
                .nivel("Graduação")
                .coordenador(coordenadorBase)
                .build());

        Optional<CursoEntity> resposta1 = cursoJpaRepository.findById(curso1.getIdCurso());
        Optional<CursoEntity> resposta2 = cursoJpaRepository.findById(curso2.getIdCurso());

        // resultado1
        assertTrue(resposta1.isPresent());
        CursoEntity cursoEncontrado1 = resposta1.get();
        assertEquals(curso1.getIdCurso(), cursoEncontrado1.getIdCurso());
        assertEquals(curso1.getNome(), cursoEncontrado1.getNome());
        assertEquals(curso1.getNivel(), cursoEncontrado1.getNivel());
        assertEquals(curso1.getCoordenador(), cursoEncontrado1.getCoordenador());

        // resultado2
        assertTrue(resposta2.isPresent());
        CursoEntity cursoEncontrado2 = resposta2.get();
        assertEquals(curso2.getIdCurso(), cursoEncontrado2.getIdCurso());
        assertEquals(curso2.getNome(), cursoEncontrado2.getNome());
        assertEquals(curso2.getNivel(), cursoEncontrado2.getNivel());
        assertEquals(curso2.getCoordenador(), cursoEncontrado2.getCoordenador());
    }

    @Test
    public void naoDeveSalvarCursoSemNomeOuNivel() {
        CursoEntity curso = CursoEntity.builder()
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
        CursoEntity curso1 = cursoJpaRepository.save(CursoEntity.builder()
                .nome("curso nome 1")
                .nivel("Graduação")
                .coordenador(coordenadorBase)
                .build());
        CursoEntity curso2 = cursoJpaRepository.save(CursoEntity.builder()
                .nome("curso nome 2")
                .nivel("Graduação")
                .build());
        CursoEntity curso3 = cursoJpaRepository.save(CursoEntity.builder()
                .nome("Design de Móveis")
                .nivel("Técnico")
                .coordenador(coordenadorBase)
                .build());
        cursoJpaRepository.save(curso1);
        cursoJpaRepository.save(curso2);
        cursoJpaRepository.save(curso3);

        List<CursoEntity> cursos = cursoJpaRepository.findAll();

        assertFalse(cursos.isEmpty());
        assertEquals(3, cursos.size());
        assertTrue(cursos.contains(curso1));
        assertTrue(cursos.contains(curso2));
        assertTrue(cursos.contains(curso3));
    }
}
