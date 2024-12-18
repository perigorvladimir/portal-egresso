package com.ufma.portalegressos.database;

import com.ufma.portalegressos.application.domain.Coordenador;
import com.ufma.portalegressos.application.out.CoordenadorJpaRepository;
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
                .tipo("coordenador")
                .login("login")
                .senha("senha")
                .build();
        Coordenador coordenadorSalvo = coordenadorJpaRepository.save(coordenador);

        assertNotNull(coordenadorSalvo);
        assertEquals(coordenador.getTipo(), coordenadorSalvo.getTipo());
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
                .build();

        // Salva a entidade
        Coordenador coordenadorSalvo = coordenadorJpaRepository.saveAndFlush(coordenador);
        // precisa dar clear para limpar o cache e a entidade vir corretamente com valor default
        entityManager.clear();
        // busca para atualizar o estado da entidade
        Coordenador coordenadorAtualizado = coordenadorJpaRepository.findById(coordenadorSalvo.getIdCoordenador())
                .orElseThrow(() -> new RuntimeException("Coordenador não encontrado"));

        assertEquals("egresso", coordenadorAtualizado.getTipo());

        coordenador = Coordenador.builder()
                .login("login")
                .senha("senha")
                .tipo(null)
                .build();
        Coordenador coordenadorSalvo2 = coordenadorJpaRepository.saveAndFlush(coordenador);
        entityManager.clear();
        Coordenador coordenadorAtualizado2 = coordenadorJpaRepository.findById(coordenadorSalvo2.getIdCoordenador())
                .orElseThrow(() -> new RuntimeException("Coordenador não encontrado"));
        assertEquals("egresso", coordenadorAtualizado2.getTipo());
    }

    @Test
    @Transactional
    public void deveVerificarBuscarCoordenadorPorId(){
        Coordenador coordenadorCriado1 = coordenadorJpaRepository.save(Coordenador.builder()
                .login("login 1")
                .senha("123")
                .build());
        Coordenador coordenadorCriado2 = coordenadorJpaRepository.save(Coordenador.builder()
                .tipo("admin")
                .login("login 2")
                .senha("12345")
                .build());
        entityManager.clear();

        Optional<Coordenador> resultado1 = coordenadorJpaRepository.findById(coordenadorCriado1.getIdCoordenador());
        Optional<Coordenador> resultado2 = coordenadorJpaRepository.findById(coordenadorCriado2.getIdCoordenador());


        // resultado 1
        assertTrue(resultado1.isPresent());
        Coordenador coordenadorEncontrado1 = resultado1.get();
        System.out.println(coordenadorEncontrado1.getTipo());
        assertEquals(coordenadorCriado1.getIdCoordenador(), coordenadorEncontrado1.getIdCoordenador());
        assertEquals("egresso", coordenadorEncontrado1.getTipo());
        assertEquals(coordenadorCriado1.getLogin(), coordenadorEncontrado1.getLogin());

        // resultado 2
        assertTrue(resultado2.isPresent());
        Coordenador coordenadorEncontrado2 = resultado2.get();
        assertEquals(coordenadorCriado2.getIdCoordenador(), coordenadorEncontrado2.getIdCoordenador());
        assertEquals(coordenadorCriado2.getTipo(), coordenadorEncontrado2.getTipo());
        assertEquals(coordenadorCriado2.getLogin(), coordenadorEncontrado2.getLogin());
    }

    @Test
    public void naoDeveSalvarSemLoginOuSenha(){
        Coordenador coordenador = Coordenador.builder()
                .login("loginteste")
                .tipo(null)
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
                .senha("123")
                .build();
        Coordenador coordenador2 = Coordenador.builder()
                        .login("login2")
                        .senha("1234")
                        .tipo("admin")
                        .build();
        Coordenador coordenador3 = Coordenador.builder()
                .login("login3")
                .senha("12345")
                .tipo("super-admin")
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
