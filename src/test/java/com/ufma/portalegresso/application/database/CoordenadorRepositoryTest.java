package com.ufma.portalegresso.application.database;

import com.ufma.portalegresso.application.domain.Coordenador;
import com.ufma.portalegresso.application.out.CoordenadorJpaRepository;
import jakarta.persistence.EntityManager;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
public class CoordenadorRepositoryTest {
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private CoordenadorJpaRepository coordenadorJpaRepository;

    @Test
    @Transactional
    public void deveSalvarCoordenadorFluxoPadrao(){
        Coordenador coordenador = Coordenador.builder()
                .login("login")
                .senha("senha")
                .nome("Geraldo")
                .build();
        Coordenador coordenadorSalvo = coordenadorJpaRepository.save(coordenador);

        assertNotNull(coordenadorSalvo);
        assertEquals(coordenador.getRole(), coordenadorSalvo.getRole());
        assertEquals(coordenador.getLogin(), coordenadorSalvo.getLogin());
        assertEquals(coordenador.getCursos(), coordenadorSalvo.getCursos());
        assertEquals(coordenador.getSenha(), coordenadorSalvo.getSenha());
    }
    @Test
    @Transactional
    public void deveVerificarValorDefaultDaRole() {
        Coordenador coordenador = Coordenador.builder()
                .login("login")
                .senha("senha")
                .nome("Igor")
                .build();

        // Salva a entidade
        Coordenador coordenadorSalvo = coordenadorJpaRepository.saveAndFlush(coordenador);
        // precisa dar clear para limpar o cache e a entidade vir corretamente com valor default
        entityManager.clear();
        // busca para atualizar o estado da entidade
        Coordenador coordenadorAtualizado = coordenadorJpaRepository.findById(coordenadorSalvo.getIdCoordenador())
                .orElseThrow(() -> new RuntimeException("Coordenador não encontrado"));

        assertEquals("ROLE_ADMIN", coordenadorAtualizado.getRole());

        coordenador = Coordenador.builder()
                .login("login2")
                .senha("senha")
                .nome("Igor")
                .build();
        Coordenador coordenadorSalvo2 = coordenadorJpaRepository.saveAndFlush(coordenador);
        entityManager.clear();
        Coordenador coordenadorAtualizado2 = coordenadorJpaRepository.findById(coordenadorSalvo2.getIdCoordenador())
                .orElseThrow(() -> new RuntimeException("Coordenador não encontrado"));
        assertEquals("ROLE_ADMIN", coordenadorAtualizado2.getRole());
    }

    @Test
    @Transactional
    public void deveBuscarCoordenadorPorId(){
        Coordenador coordenadorCriado1 = coordenadorJpaRepository.save(Coordenador.builder()
                .login("login 1")
                .senha("123")
                .nome("nome1")
                .build());
        Coordenador coordenadorCriado2 = coordenadorJpaRepository.save(Coordenador.builder()
                .login("login 2")
                .senha("12345")
                .nome("nome2")
                .build());
        entityManager.clear();

        Optional<Coordenador> resultado1 = coordenadorJpaRepository.findById(coordenadorCriado1.getIdCoordenador());
        Optional<Coordenador> resultado2 = coordenadorJpaRepository.findById(coordenadorCriado2.getIdCoordenador());


        // resultado 1
        assertTrue(resultado1.isPresent());
        Coordenador coordenadorEncontrado1 = resultado1.get();
        assertEquals(coordenadorCriado1.getIdCoordenador(), coordenadorEncontrado1.getIdCoordenador());
        assertEquals("ROLE_ADMIN", coordenadorEncontrado1.getRole());
        assertEquals(coordenadorCriado1.getLogin(), coordenadorEncontrado1.getLogin());

        // resultado 2
        assertTrue(resultado2.isPresent());
        Coordenador coordenadorEncontrado2 = resultado2.get();
        assertEquals(coordenadorCriado2.getIdCoordenador(), coordenadorEncontrado2.getIdCoordenador());
        assertEquals("ROLE_ADMIN", coordenadorEncontrado2.getRole());
        assertEquals(coordenadorCriado2.getLogin(), coordenadorEncontrado2.getLogin());
    }

    @Test
    public void naoDeveSalvarSemLoginOuSenha(){
        Coordenador coordenador = Coordenador.builder()
                .login("loginteste")
                .nome("Geraldo Braz")
                .build();

        DataIntegrityViolationException exceptionSemSenha = assertThrows(DataIntegrityViolationException.class, () -> coordenadorJpaRepository.save(coordenador));
        assertEquals("senha", exceptionSemSenha.getCause().getCause().getLocalizedMessage().split("\"")[1].toLowerCase());

        coordenador.setSenha("senhaTeste");
        coordenador.setLogin(null);
        DataIntegrityViolationException exceptionSemLogin = assertThrows(DataIntegrityViolationException.class, () -> coordenadorJpaRepository.save(coordenador));
        assertEquals("login", exceptionSemLogin.getCause().getCause().getLocalizedMessage().split("\"")[1].toLowerCase());

        coordenador.setLogin("loginteste");
        coordenador.setNome(null);
        DataIntegrityViolationException exceptionSemNome = assertThrows(DataIntegrityViolationException.class, () -> coordenadorJpaRepository.save(coordenador));
        assertEquals("nome", exceptionSemNome.getCause().getCause().getLocalizedMessage().split("\"")[1].toLowerCase());

        coordenador.setNome("Geraldo Braz");
        Coordenador coordSalvo = assertDoesNotThrow(() -> coordenadorJpaRepository.save(coordenador));
        coordenadorJpaRepository.deleteById(coordSalvo.getIdCoordenador());
    }

    @Test
    @Transactional
    public void deveVerificarBuscarTodosCoordenadores(){
        Coordenador coordenador1 = Coordenador.builder()
                .login("login")
                .nome("nome1")
                .senha("123")
                .build();
        Coordenador coordenador2 = Coordenador.builder()
                        .login("login2")
                        .senha("1234")
                        .nome("nome2")
                        .build();
        Coordenador coordenador3 = Coordenador.builder()
                .login("login3")
                .senha("12345")
                .nome("nome2")
                .build();
        coordenadorJpaRepository.save(coordenador1);
        coordenadorJpaRepository.save(coordenador2);
        coordenadorJpaRepository.save(coordenador3);

        List<Coordenador> resposta = coordenadorJpaRepository.findAll();

        assertFalse(resposta.isEmpty());
        assertEquals(3, resposta.size());
        assertTrue(resposta.contains(coordenador1));
    }

    @Test
    @Transactional
    public void deveAtualizarCoordenadorFluxoPadrao(){
        Coordenador coordenadorSalvo = coordenadorJpaRepository.save(Coordenador.builder()
                .nome("Igor")
                .login("login")
                .senha("senha")
                .build());
        Coordenador coordenadorAtualizado = Coordenador.builder()
                .idCoordenador(coordenadorSalvo.getIdCoordenador())
                .nome("nomeAtualizado")
                .login("loginAtualizado")
                .senha("senhaAtualizado")
                .build();

        coordenadorJpaRepository.save(coordenadorAtualizado);

        Optional<Coordenador> resultado = coordenadorJpaRepository.findById(coordenadorAtualizado.getIdCoordenador());

        assertTrue(resultado.isPresent());
        Coordenador coordenadorEncontrado = resultado.get();
        assertEquals(coordenadorAtualizado.getIdCoordenador(), coordenadorEncontrado.getIdCoordenador());
        assertEquals(coordenadorAtualizado.getRole(), coordenadorEncontrado.getRole());
        assertEquals(coordenadorAtualizado.getLogin(), coordenadorEncontrado.getLogin());
        assertEquals(coordenadorAtualizado.getCursos(), coordenadorEncontrado.getCursos());
        assertEquals(coordenadorAtualizado.getSenha(), coordenadorEncontrado.getSenha());
    }

    @Test
    @Transactional
    public void deveDeletarCoordenadorPorId(){
        Coordenador coordenadorSalvo = coordenadorJpaRepository.save(Coordenador.builder()
                .login("login")
                .senha("senha")
                .nome("Igor")
                .build());
        coordenadorJpaRepository.deleteById(coordenadorSalvo.getIdCoordenador());

        assertThrows(NoSuchElementException.class, () -> coordenadorJpaRepository.findById(coordenadorSalvo.getIdCoordenador()).get());
    }

    @Test
    @Transactional
    public void naoDeveSalvarCoordenadorComLoginDuplicado(){
        Coordenador coordenadorSalvo = coordenadorJpaRepository.save(Coordenador.builder()
                .login("login")
                .senha("senha")
                .nome("Igor")
                .build());
        Coordenador coord = Coordenador.builder()
                .login("login")
                .senha("123")
                .nome("Geraldo")
                .build();

        assertThrows(DataIntegrityViolationException.class, () -> coordenadorJpaRepository.save(coord));
    }
}
