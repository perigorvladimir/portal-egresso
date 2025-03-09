package com.ufma.portalegresso.application.services;

import com.ufma.portalegresso.application.domain.Coordenador;
import com.ufma.portalegresso.application.usecases.coordenador.SalvarCoordenadorUC;
import com.ufma.portalegresso.application.usecases.coordenador.UpdateCoordenadorUC;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class CoordenadorServiceTest {
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private CoordenadorService service;

    @Test
    @Transactional
    public void deveSalvarCoordenadorFluxoPadrao(){
        SalvarCoordenadorUC.Request request = SalvarCoordenadorUC.Request.builder()
                .nome("nome coordenador")
                .login("login")
                .senha("senhaOitoDigitos")
                .build();
        Coordenador coordenadorSalvo = service.salvarCoordenador(request, "noop");

        entityManager.clear();
        Coordenador coordenador = entityManager.find(Coordenador.class, coordenadorSalvo.getIdCoordenador());
        assertNotNull(coordenadorSalvo);
        assertNotNull(coordenadorSalvo.getIdCoordenador());
        assertEquals("ROLE_ADMIN", coordenador.getRole());
        assertEquals(request.getNome(), coordenadorSalvo.getNome());
        assertEquals(request.getLogin(), coordenadorSalvo.getLogin());
        assertEquals(request.getSenha(), coordenadorSalvo.getSenha().split("}")[1]);
    }

    @Test
    @Transactional
    public void deveGerarErroAoTentarSalvarComLoginJaExistente() {
        SalvarCoordenadorUC.Request coord1 = SalvarCoordenadorUC.Request.builder()
                .nome("nome coordenador")
                .login("login")
                .senha("senhaOitoDigitos")
                .build();
        SalvarCoordenadorUC.Request coord2 = SalvarCoordenadorUC.Request.builder()
                .nome("nome coordenado 2")
                .login("login")
                .senha("12345678")
                .build();
        service.salvarCoordenador(coord1, "noop");
        assertThrows(IllegalArgumentException.class, () -> service.salvarCoordenador(coord2, "noop"));
    }
    @Test
    @Transactional
    public void deveTestarSeEstaConcatenandoAlgoritmoCriptografiaNaSenha(){
        SalvarCoordenadorUC.Request request1 = SalvarCoordenadorUC.Request.builder()
                .nome("nome coordenador")
                .login("login")
                .senha("12345678")
                .build();
        SalvarCoordenadorUC.Request request2 = SalvarCoordenadorUC.Request.builder()
                .nome("nome coordenado 2")
                .login("login2")
                .senha("12345678")
                .build();
        Coordenador coord1 = service.salvarCoordenador(request1, "noop");
        Coordenador coord2 = service.salvarCoordenador(request2, "bcrypt");

        assertTrue(coord1.getSenha().contains("{noop}"));
        assertTrue(coord2.getSenha().contains("{bcrypt}"));
    }
    @Test
    public void deveGerarErroSenhaMenorQueOito(){
        SalvarCoordenadorUC.Request request = SalvarCoordenadorUC.Request.builder()
                .nome("nome coordenador")
                .login("login")
                .senha("1234567")
                .build();
        assertThrows(IllegalArgumentException.class, () -> service.salvarCoordenador(request, "noop"));
    }
    @Test
    public void deveGerarErroSenhaNull(){
        SalvarCoordenadorUC.Request request = SalvarCoordenadorUC.Request.builder()
                .nome("nome coordenador")
                .login("login")
                .build();
        assertThrows(IllegalArgumentException.class, () -> service.salvarCoordenador(request, "noop"));
    }
    @Test
    @Transactional
    public void deveCriptografarComBCryptAoMandarAlgoritmoCriptografiaNulo(){
        SalvarCoordenadorUC.Request request = SalvarCoordenadorUC.Request.builder()
                .nome("nome coordenador")
                .login("login")
                .senha("12345678")
                .build();
        Coordenador coord = service.salvarCoordenador(request, null);
        System.out.println(coord.getSenha());
        assertTrue(coord.getSenha().contains("{bcrypt}"));
    }
    @Test
    @Transactional
    public void deveGerarErroAoMandarAlgoritmoCriptografiaInexistente(){
        SalvarCoordenadorUC.Request request = SalvarCoordenadorUC.Request.builder()
                .nome("nome coordenador")
                .login("login")
                .senha("12345678")
                .build();
        assertThrows(IllegalArgumentException.class, () -> service.salvarCoordenador(request, "alg"));
    }
    @Test
    @Transactional
    public void deveBuscarTodosCoordenadores() {
        Coordenador coord1 = criarCoordenadorTestes();
        Coordenador coordenador2 = service.salvarCoordenador(SalvarCoordenadorUC.Request.builder().login("login2").senha("12345678").nome("nome coordenador 2").build(), "noop");
        Coordenador coordenador3 = service.salvarCoordenador(SalvarCoordenadorUC.Request.builder().login("login3").senha("12345678").nome("nome coordenador 3").build(), "noop");

        List<Coordenador> resposta = service.buscarTodosCoordenadores();

        assertFalse(resposta.isEmpty());
        assertEquals(3, resposta.size());
        assertTrue(resposta.contains(coord1));
        assertTrue(resposta.contains(coordenador2));
        assertTrue(resposta.contains(coordenador3));
    }
    @Test
    @Transactional
    public void deveBuscarCoordenadorPorIdFluxoPadrao() {
        Coordenador coord = criarCoordenadorTestes();

        Coordenador coordEncontrado = service.buscarCoordenadorPorId(coord.getIdCoordenador());
        assertNotNull(coordEncontrado);
        assertEquals(coord.getIdCoordenador(), coordEncontrado.getIdCoordenador());
        assertEquals(coord.getRole(), coordEncontrado.getRole());
        assertEquals(coord.getNome(), coordEncontrado.getNome());
        assertEquals(coord.getLogin(), coordEncontrado.getLogin());
        assertEquals(coord.getSenha(), coordEncontrado.getSenha());
    }
    @Test
    public void deveGerarErroAoBuscarCoordenadorPorIdInexistente(){
        assertThrows(EntityNotFoundException.class, () -> service.buscarCoordenadorPorId(100));
    }
    @Test
    public void deveGerarErroAoBuscarCoordenadorPorIdNull(){
        assertThrows(IllegalArgumentException.class, () -> service.buscarCoordenadorPorId(null));
    }
    @Test
    @Transactional
    public void deveAtualizarCoordenadorFluxoPadrao(){
        Coordenador coordCriado = criarCoordenadorTestes();

        UpdateCoordenadorUC.Request coordAtualizado = UpdateCoordenadorUC.Request.builder().nome("coordenador atualizado").build();
        service.updateCoordenador(coordCriado.getIdCoordenador(), coordAtualizado);

        Coordenador coordEncontrado = service.buscarCoordenadorPorId(coordCriado.getIdCoordenador());

        assertNotNull(coordEncontrado);
        assertEquals(coordCriado.getIdCoordenador(), coordEncontrado.getIdCoordenador());
        assertEquals(coordAtualizado.getNome(), coordEncontrado.getNome());
    }
    @Test
    public void deveGerarErroAoAtualizarCoordenadorMandandoIdNull(){
        UpdateCoordenadorUC.Request coordAtualizado = UpdateCoordenadorUC.Request.builder().nome("coordenador atualizado").build();
        assertThrows(IllegalArgumentException.class, () -> service.updateCoordenador(null, coordAtualizado));
    }
    @Test
    public void deveGerarErroAoAtualizarCoordenadorInexistente(){
        UpdateCoordenadorUC.Request coordAtualizado = UpdateCoordenadorUC.Request.builder().nome("coordenador atualizado").build();
        assertThrows(EntityNotFoundException.class, () -> service.updateCoordenador(100, coordAtualizado));
    }
    @Test
    public void deveDeletarCoordenadorPorId(){
        Coordenador coord = criarCoordenadorTestes();
        service.deletarCoordenadorPorId(coord.getIdCoordenador());
        assertThrows(EntityNotFoundException.class, () -> service.buscarCoordenadorPorId(coord.getIdCoordenador()));
    }
    @Test
    @Transactional
    public void deveVerificarLoadByUserNameFluxoPadrao(){
        Coordenador coord = criarCoordenadorTestes();

        UserDetails user = service.loadUserByUsername(coord.getLogin());
        assertNotNull(user);
        assertEquals(1, user.getAuthorities().size());
        assertEquals(coord.getLogin(), user.getUsername());
        assertEquals(coord.getSenha(), user.getPassword());
    }
    @Test
    public void deveGerarErroAoLoadByUserNameInexistente(){
        assertThrows(UsernameNotFoundException.class, () -> service.loadUserByUsername("loginNaoCadastrado"));
    }
    @Test
    public void deveGerarERroAoLoadByUserNameParametroNull(){
        assertThrows(UsernameNotFoundException.class, () -> service.loadUserByUsername(null));
    }

    private Coordenador criarCoordenadorTestes(){
        SalvarCoordenadorUC.Request request = SalvarCoordenadorUC.Request.builder()
                .nome("nome coordenador")
                .login("login")
                .senha("senhaOitoDigitos")
                .build();
        return service.salvarCoordenador(request, "noop");
    }
}
