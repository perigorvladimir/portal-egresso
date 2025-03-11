package com.ufma.portalegresso.application.database;

import com.ufma.portalegresso.application.domain.Curso;
import com.ufma.portalegresso.application.domain.CursoEgresso;
import com.ufma.portalegresso.application.domain.Egresso;
import com.ufma.portalegresso.application.domain.TipoNivel;
import com.ufma.portalegresso.application.domain.relacionamentos.CursoEgressoId;
import com.ufma.portalegresso.application.out.CursoEgressoJpaRepository;
import com.ufma.portalegresso.application.out.CursoJpaRepository;
import com.ufma.portalegresso.application.out.EgressoJpaRepository;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class EgressoRepositoryTest {
    private static final Logger logger = LoggerFactory.getLogger(EgressoRepositoryTest.class);
    @Autowired
    private EgressoJpaRepository egressoJpaRepository;
    @Autowired
    private CursoJpaRepository cursoJpaRepository;
    @Autowired
    private CursoEgressoJpaRepository cursoEgressoJpaRepository;

    @Test
    @Transactional
    public void deveSalvarEgressoFluxoPadrao(){
        // CENARIO
        Egresso egresso = Egresso.builder()
                .nome("Igor Vladimir Cunha de Alencar")
                .email("igor.vladimir@discente.ufma.br")
                .curriculo("curriculo teste curriculo teste curriculo teste curriculo teste curriculo teste")
                .descricao("graduando CP")
                .linkedin("https://www.linkedin.com/in/igorvalencar/")
                .instagram("instagram_teste").build();

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
    public void deveBuscarEgressoPorId(){
        // CENARIO
        Integer idEgresso = egressoJpaRepository.save(Egresso.builder()
                                                .nome("Igor Vladimir Cunha de Alencar")
                                                .email("igor.vladimir@discente.ufma.br")
                                                .curriculo("curriculo teste curriculo teste curriculo teste curriculo teste curriculo teste")
                                                .descricao("graduando CP")
                                                .linkedin("https://www.linkedin.com/in/igorvalencar/")
                                                .instagram("instagram_teste").build()).getIdEgresso();
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
        DataIntegrityViolationException exceptionSemNome = assertThrows(DataIntegrityViolationException.class, () -> egressoJpaRepository.save(egresso));
        assertEquals("nome", exceptionSemNome.getCause().getCause().getLocalizedMessage().split("\"")[1].toLowerCase());

        egresso.setNome("Igor Vladimir");
        egresso.setEmail(null);
        DataIntegrityViolationException exceptionSemEmail = assertThrows(DataIntegrityViolationException.class, () -> egressoJpaRepository.save(egresso));

        egresso.setEmail("igor.vladimir@discente.ufma.br");
        Egresso egressoSalvo = assertDoesNotThrow(() -> egressoJpaRepository.save(egresso));
        egressoJpaRepository.deleteById(egressoSalvo.getIdEgresso());
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
                .instagram("instagram_teste").build();
        Egresso egresso2 = Egresso.builder()
                .nome("Fulano da Silva")
                .email("fulano.silva@discente.ufma.br")
                .curriculo("curriculo de Fulano da Silva")
                .descricao("descrição do FUlano da Silva")
                .linkedin("https://www.linkedin.com/in/fulanoDaSilva/")
                .instagram("fulano_silva").build();
        Egresso egresso3 = Egresso.builder()
                .nome("Fulano da Silva")
                .email("fulano.silva@discente.ufma.br")
                .curriculo("curriculo de Fulano da Silva")
                .descricao("descrição do FUlano da Silva")
                .linkedin("https://www.linkedin.com/in/fulanoDaSilva/")
                .instagram("fulano_silva").build();
        egressoJpaRepository.save(egresso1);
        egressoJpaRepository.save(egresso2);
        egressoJpaRepository.save(egresso3);

        // ACAO
        List<Egresso> resultado = egressoJpaRepository.findAll();
        // VALIDACAO
        assertFalse(resultado.isEmpty());
        assertEquals(3, resultado.size());
    }

    @Test
    @Transactional
    public void deveAtualizarEgressoFluxoPadrao(){
        // CENARIO
        Egresso egressoSalvo = egressoJpaRepository.save(Egresso.builder()
                .nome("Igor Vladimir Cunha de Alencar")
                .email("igor.vladimir@discente.ufma.br")
                .curriculo("curriculo teste curriculo teste curriculo teste curriculo teste curriculo teste")
                .descricao("graduando CP")
                .linkedin("https://www.linkedin.com/in/igorvalencar/")
                .instagram("instagram_teste")
                .build());
        Egresso egressoAtualizado = Egresso.builder()
                .nome("Geraldo Braz Junior")
                .email("geraldo.braz@discente.ufma.br")
                .curriculo("curriculo atualizado")
                .descricao("graduando CP atualizado")
                .linkedin("https://www.linkedin.com/in/igorvalencar/atualizado")
                .instagram("instagram_teste atualizado")
                .build();

        egressoJpaRepository.save(egressoAtualizado);

        // AÇÃO
        Optional<Egresso> resultado = egressoJpaRepository.findById(egressoAtualizado.getIdEgresso());

        // VERIFICAÇÃO
        assertTrue(resultado.isPresent());
        Egresso egressoEncontrado = resultado.get();
        assertEquals(egressoAtualizado.getIdEgresso(), egressoEncontrado.getIdEgresso());
        assertEquals(egressoAtualizado.getNome(), egressoEncontrado.getNome());
        assertEquals(egressoAtualizado.getEmail(), egressoEncontrado.getEmail());
        assertEquals(egressoAtualizado.getCurriculo(), egressoEncontrado.getCurriculo());
        assertEquals(egressoAtualizado.getDescricao(), egressoEncontrado.getDescricao());
        assertEquals(egressoAtualizado.getLinkedin(), egressoEncontrado.getLinkedin());
        assertEquals(egressoAtualizado.getInstagram(), egressoEncontrado.getInstagram());
    }

    @Test
    @Transactional
    public void deveDeletarEgressoPorId(){
        Egresso egressoSalvo = egressoJpaRepository.save(Egresso.builder()
                .nome("Igor Vladimir Cunha de Alencar")
                .email("igor.vladimir@discente.ufma.br")
                .curriculo("curriculo teste curriculo teste curriculo teste curriculo teste curriculo teste")
                .descricao("graduando CP")
                .linkedin("https://www.linkedin.com/in/igorvalencar/")
                .instagram("instagram_teste")
                .build());

        // AÇÃO
        egressoJpaRepository.deleteById(egressoSalvo.getIdEgresso());

        // VERIFICAÇÃO
        assertThrows(NoSuchElementException.class, () -> egressoJpaRepository.findById(egressoSalvo.getIdEgresso()).get());
    }
    @Test
    @Transactional
    public void deveEgressoPoderTerVariosCursos(){
        Egresso egressoSalvo = egressoJpaRepository.save(Egresso.builder()
                .email("igor@exemplo.com")
                .nome("igor")
                .build());

        Curso curso = cursoJpaRepository.save(Curso.builder().tipoNivel(TipoNivel.GRADUACAO).nome("Ciência da Computação").build());
        Curso curso2 = cursoJpaRepository.save(Curso.builder().tipoNivel(TipoNivel.GRADUACAO).nome("Engenharia da Computação").build());

        CursoEgressoId id = CursoEgressoId.builder().idCurso(curso.getIdCurso()).idEgresso(egressoSalvo.getIdEgresso()).build();
        CursoEgresso cursoEgressoSalvo = cursoEgressoJpaRepository.save(CursoEgresso.builder()
                .id(id)
                .egresso(egressoSalvo)
                .curso(curso)
                .anoInicio(2015)
                .anoFim(2019)
                .build());
        CursoEgressoId id2 = CursoEgressoId.builder().idCurso(curso2.getIdCurso()).idEgresso(egressoSalvo.getIdEgresso()).build();
        CursoEgresso cursoEgressoSalvo2 = cursoEgressoJpaRepository.save(CursoEgresso.builder()
                .id(id2)
                .egresso(egressoSalvo)
                .curso(curso2)
                .anoInicio(2020)
                .anoFim(null)
                .build());

        assertEquals(egressoSalvo.getIdEgresso(), cursoEgressoSalvo.getId().getIdEgresso());
        assertEquals(egressoSalvo.getIdEgresso(), cursoEgressoSalvo2.getId().getIdEgresso());
    }

}
