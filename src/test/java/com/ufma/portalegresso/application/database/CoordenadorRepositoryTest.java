package com.ufma.portalegresso.application.database;

import com.ufma.portalegresso.application.domain.Coordenador;
import com.ufma.portalegresso.application.out.CoordenadorJpaRepository;
import jakarta.persistence.EntityManager;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Profile("test")
public class CoordenadorRepositoryTest {
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private CoordenadorJpaRepository coordenadorJpaRepository;

    @Test
    @Transactional
    public void deveVerificarSalvarCoordenador(){
        Coordenador coordenador = Coordenador.builder()
                .role("coordenador")
                .login("login")
                .senha("senha")
                .nome("Geraldo")
                .build();
        Coordenador coordenadorSalvo = coordenadorJpaRepository.save(coordenador);

        assertNotNull(coordenadorSalvo);
        assertEquals(coordenador.getRole(), coordenadorSalvo.getRole());
        assertEquals(coordenador.getLogin(), coordenadorSalvo.getLogin());
        assertEquals(coordenador.getCursos(), coordenadorSalvo.getCursos());
        // nao verifica login pois vai ser salvo criptografado
    }
    @Test
    @Transactional
    public void deveVerificarValorDefaultDoTipo() {
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
                .role(null)
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
    public void deveVerificarBuscarCoordenadorPorId(){
        Coordenador coordenadorCriado1 = coordenadorJpaRepository.save(Coordenador.builder()
                .login("login 1")
                .senha("123")
                .nome("nome1")
                .build());
        Coordenador coordenadorCriado2 = coordenadorJpaRepository.save(Coordenador.builder()
                .role("admin")
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
        assertEquals(coordenadorCriado2.getRole(), coordenadorEncontrado2.getRole());
        assertEquals(coordenadorCriado2.getLogin(), coordenadorEncontrado2.getLogin());
    }

    @Test
    public void naoDeveSalvarSemLoginOuSenha(){
        Coordenador coordenador = Coordenador.builder()
                .login("loginteste")
                .role(null)
                .build();

        assertThrows(ConstraintViolationException.class, () -> coordenadorJpaRepository.save(coordenador));
        coordenador.setSenha("senhaTeste");
        coordenador.setLogin(null);
        assertThrows(ConstraintViolationException.class, () -> coordenadorJpaRepository.save(coordenador));
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
                        .role("admin")
                        .build();
        Coordenador coordenador3 = Coordenador.builder()
                .login("login3")
                .senha("12345")
                .nome("nome2")
                .role("super-admin")
                .build();
        coordenadorJpaRepository.save(coordenador1);
        coordenadorJpaRepository.save(coordenador2);
        coordenadorJpaRepository.save(coordenador3);

        List<Coordenador> resposta = coordenadorJpaRepository.findAll();

        assertFalse(resposta.isEmpty());
        assertEquals(3, resposta.size());
        assertTrue(resposta.contains(coordenador1));
    }

}
