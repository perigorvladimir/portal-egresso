package com.ufma.portalegresso.application.services;

import com.ufma.portalegresso.application.domain.Curso;
import com.ufma.portalegresso.application.domain.CursoEgresso;
import com.ufma.portalegresso.application.domain.Egresso;
import com.ufma.portalegresso.application.domain.relacionamentos.CursoEgressoId;
import com.ufma.portalegresso.application.out.CursoEgressoJpaRepository;
import com.ufma.portalegresso.application.usecases.curso.SalvarCursoUC;
import com.ufma.portalegresso.application.usecases.egresso.SalvarEgressoUC;
import com.ufma.portalegresso.application.usecases.egresso.UpdateEgressoUC;
import com.ufma.portalegresso.application.usecases.egresso.VincularCursoUC;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
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
public class EgressoServiceTest {
    @Autowired
    private EgressoService service;
    @Autowired
    private CursoEgressoJpaRepository cursoEgressoJpaRepository;
    private static Curso cursoBase;
    private static Curso cursoBase2;
    @Autowired
    private EntityManager entityManager;

    @BeforeAll
    public static void setup(@Autowired CursoService cursoService) {
        SalvarCursoUC.Request request  = SalvarCursoUC.Request.builder()
                .nome("Ciência da Computação")
                .tipoNivel("GRADUACAO")
                .idCoordenador(null)
                .build();
        SalvarCursoUC.Request request2  = SalvarCursoUC.Request.builder()
                .nome("Engenharia da Computação")
                .tipoNivel("GRADUACAO")
                .idCoordenador(null)
                .build();

        cursoBase = cursoService.salvarCurso(request);
        cursoBase2 = cursoService.salvarCurso(request2);
    }
    @AfterAll
    public static void cleanUp(@Autowired CursoService cursoService) {
        cursoService.deletarCursoPorId(cursoBase.getIdCurso());
        cursoService.deletarCursoPorId(cursoBase2.getIdCurso());
    }

    @Test
    @Transactional
    public void deveSalvarEgressoFluxoPadrao() {
        SalvarEgressoUC.Request egressoRequest = SalvarEgressoUC.Request.builder()
                .nome("Igor")
                .email("QYv3I@example.com")
                .descricao("Descrição")
                .foto("Foto")
                .linkedin("Linkedin")
                .instagram("Instagram")
                .curriculo("Curriculo")
                .idCurso(cursoBase.getIdCurso())
                .anoInicioCurso(2020)
                .anoFimCurso(2023)
                .build();

        Egresso egressoSalvo = service.salvarEgresso(egressoRequest);
        entityManager.clear();
        Egresso egressoEncontrado = service.buscarEgressoPorId(egressoSalvo.getIdEgresso());

        assertNotNull(egressoSalvo);
        assertNotNull(egressoSalvo.getIdEgresso());
        assertEquals(egressoRequest.getNome(), egressoEncontrado.getNome());
        assertEquals(egressoRequest.getEmail(), egressoEncontrado.getEmail());
        assertEquals(egressoRequest.getDescricao(), egressoEncontrado.getDescricao());
        assertEquals(egressoRequest.getFoto(), egressoEncontrado.getFoto());
        assertEquals(egressoRequest.getLinkedin(), egressoEncontrado.getLinkedin());
        assertEquals(egressoRequest.getInstagram(), egressoEncontrado.getInstagram());
        assertEquals(egressoRequest.getCurriculo(), egressoEncontrado.getCurriculo());
        assertFalse(egressoEncontrado.getCursoEgressos().isEmpty());
        assertEquals(1, egressoEncontrado.getCursoEgressos().size());
        assertEquals(egressoRequest.getIdCurso(), egressoEncontrado.getCursoEgressos().getFirst().getCurso().getIdCurso());
        assertEquals(egressoRequest.getAnoInicioCurso(), egressoEncontrado.getCursoEgressos().getFirst().getAnoInicio());
        assertEquals(egressoRequest.getAnoFimCurso(), egressoEncontrado.getCursoEgressos().getFirst().getAnoFim());
    }
    @Test
    public void deveGerarErroAoSalvarEgressoAnoFimCursoNull() {
        SalvarEgressoUC.Request egresso = SalvarEgressoUC.Request.builder()
                .nome("Igor")
                .email("QYv3I@example.com")
                .descricao("Descrição")
                .foto("Foto")
                .linkedin("Linkedin")
                .instagram("Instagram")
                .curriculo("Curriculo")
                .idCurso(cursoBase.getIdCurso())
                .anoInicioCurso(2020)
                .anoFimCurso(null)
                .build();

        assertThrows(IllegalArgumentException.class, () -> service.salvarEgresso(egresso));
    }
    @Test
    public void deveGerarErroAoSalvarEgressoAnoInicioCursoMaiorQueAnoFimCurso() {
        SalvarEgressoUC.Request egresso = SalvarEgressoUC.Request.builder()
                .nome("Igor")
                .email("QYv3I@example.com")
                .descricao("Descrição")
                .foto("Foto")
                .linkedin("Linkedin")
                .instagram("Instagram")
                .curriculo("Curriculo")
                .idCurso(cursoBase.getIdCurso())
                .anoInicioCurso(2020)
                .anoFimCurso(2019)
                .build();

        assertThrows(IllegalArgumentException.class, () -> service.salvarEgresso(egresso));
    }
    @Test
    public void deveGerarErroAoSalvarEgressoIdCursoInexistente() {
        SalvarEgressoUC.Request egresso = SalvarEgressoUC.Request.builder()
                .nome("Igor")
                .email("QYv3I@example.com")
                .descricao("Descrição")
                .foto("Foto")
                .linkedin("Linkedin")
                .instagram("Instagram")
                .curriculo("Curriculo")
                .idCurso(100)
                .anoInicioCurso(2020)
                .anoFimCurso(2019)
                .build();
        assertThrows(IllegalArgumentException.class, () -> service.salvarEgresso(egresso));
    }
    @Test
    public void deveGerarErroAoSalvarEgressoIdCursoNull() {
        SalvarEgressoUC.Request egresso = SalvarEgressoUC.Request.builder()
                .nome("Igor")
                .email("QYv3I@example.com")
                .descricao("Descrição")
                .foto("Foto")
                .linkedin("Linkedin")
                .instagram("Instagram")
                .curriculo("Curriculo")
                .idCurso(null)
                .anoInicioCurso(2020)
                .anoFimCurso(2019)
                .build();
        assertThrows(IllegalArgumentException.class, () -> service.salvarEgresso(egresso));
    }
    @Test
    @Transactional
    public void deveBuscarTodosEgressos() {
        Egresso egressoCriado1 = service.salvarEgresso(SalvarEgressoUC.Request.builder().nome("Igor").email("QYv3I@example.com").descricao("Descrição").instagram("Instagram").idCurso(cursoBase.getIdCurso()).anoInicioCurso(2020).anoFimCurso(2023).build());
        Egresso egressoCriado2 = service.salvarEgresso(SalvarEgressoUC.Request.builder().nome("Rick").email("exemplo@example.com").descricao("Descrição").linkedin("Linkedin").idCurso(cursoBase.getIdCurso()).anoInicioCurso(2019).anoFimCurso(2022).build());
        Egresso egressoCriado3 = service.salvarEgresso(SalvarEgressoUC.Request.builder().nome("Igor").email("exemplo2@example.com").descricao("Descrição").foto("Foto").idCurso(cursoBase.getIdCurso()).anoInicioCurso(2019).anoFimCurso(2023).build());

        List<Egresso> egressos = service.buscarTodosEgressos();

        assertFalse(egressos.isEmpty());
        assertEquals(3, egressos.size());
        assertTrue(egressos.contains(egressoCriado1));
        assertTrue(egressos.contains(egressoCriado2));
        assertTrue(egressos.contains(egressoCriado3));
    }

    @Test
    @Transactional
    public void deveBuscarEgressoPorIdFluxoPadrao() {
        Egresso egressoCriado = criarEgressoTestes();

        Egresso egressoBuscado = service.buscarEgressoPorId(egressoCriado.getIdEgresso());

        assertNotNull(egressoBuscado);
        assertEquals(egressoCriado.getIdEgresso(), egressoBuscado.getIdEgresso());
        assertEquals(egressoCriado.getNome(), egressoBuscado.getNome());
        assertEquals(egressoCriado.getEmail(), egressoBuscado.getEmail());
        assertEquals(egressoCriado.getDescricao(), egressoBuscado.getDescricao());
    }
    @Test
    public void deveGerarErroAoBuscarEgressoPorIdInexistente() {
        assertThrows(EntityNotFoundException.class, () -> service.buscarEgressoPorId(100));
    }
    @Test
    public void deveGerarErroAoBuscarEgressoPorIdNull() {
        assertThrows(IllegalArgumentException.class, () -> service.buscarEgressoPorId(null));
    }
    @Test
    @Transactional
    public void deveAtualizarEgressoFluxoPadrao(){
        Egresso egressoCriado = criarEgressoTestes();

        UpdateEgressoUC.Request request = UpdateEgressoUC.Request.builder()
                .nome("nomeAtualizado")
                .email("emailatualizado@example.com")
                .descricao("descricao atualizada")
                .curriculo("curriculo atualizado")
                .foto("foto atualizada")
                .linkedin("linkedin atualizado")
                .instagram("instagram atualizado")
                .build();

        service.updateEgresso(egressoCriado.getIdEgresso(), request);

        Egresso egressoEncontrado = service.buscarEgressoPorId(egressoCriado.getIdEgresso());

        assertNotNull(egressoEncontrado);
        assertEquals(egressoCriado.getIdEgresso(), egressoEncontrado.getIdEgresso());
        assertEquals(request.getNome(), egressoEncontrado.getNome());
        assertEquals(request.getEmail(), egressoEncontrado.getEmail());
        assertEquals(request.getDescricao(), egressoEncontrado.getDescricao());
        assertEquals(request.getCurriculo(), egressoEncontrado.getCurriculo());
        assertEquals(request.getFoto(), egressoEncontrado.getFoto());
        assertEquals(request.getLinkedin(), egressoEncontrado.getLinkedin());
        assertEquals(request.getInstagram(), egressoEncontrado.getInstagram());
    }
    @Test
    public void deveGerarErroAoAtualizaEgressoMandandoIdNull() {
        UpdateEgressoUC.Request request = UpdateEgressoUC.Request.builder()
                .nome("nomeAtualizado")
                .email("emailatualizado@example.com")
                .descricao("descricao atualizada")
                .curriculo("curriculo atualizado")
                .build();
        assertThrows(IllegalArgumentException.class, () -> service.updateEgresso(null, request));
    }
    @Test
    public void deveGerarErroAoAtualizarEgressoMandandoEgressoIdInexistente() {
        UpdateEgressoUC.Request request = UpdateEgressoUC.Request.builder()
                .nome("nomeAtualizado")
                .email("emailatualizado@example.com")
                .descricao("descricao atualizada")
                .curriculo("curriculo atualizado")
                .build();
        assertThrows(EntityNotFoundException.class, () -> service.updateEgresso(100, request));
    }
    @Test
    @Transactional
    public void deveDeletarEgressoFluxoPadrao() {
        Egresso egressoCriado = criarEgressoTestes();

        service.deletarEgressoPorId(egressoCriado.getIdEgresso());

        assertThrows(EntityNotFoundException.class, () -> service.buscarEgressoPorId(egressoCriado.getIdEgresso()));
    }


    @Test
    @Transactional
    public void deveBuscarEgressosPorCursoIdFluxoPadrao() {
        Egresso egressoCriado = service.salvarEgresso(SalvarEgressoUC.Request.builder()
                .nome("Igor")
                .email("igor@example.com")
                .descricao("descricao igor")
                .idCurso(cursoBase.getIdCurso())
                .anoInicioCurso(2020)
                .anoFimCurso(2023)
                .build());
        Egresso egressoCriado2 = service.salvarEgresso(SalvarEgressoUC.Request.builder()
                .nome("Rick")
                .email("rick@example.com")
                .descricao("descricao rick")
                .idCurso(cursoBase.getIdCurso())
                .anoInicioCurso(2019)
                .anoFimCurso(2024)
                .build());
        Egresso egressoCriado3 = service.salvarEgresso(SalvarEgressoUC.Request.builder()
                .nome("Geraldo")
                .email("geraldo@example.com")
                .descricao("descricao geraldo")
                .idCurso(cursoBase2.getIdCurso())
                .anoInicioCurso(2019)
                .anoFimCurso(2024)
                .build());

        List<Egresso> egressos = service.buscarEgressosPorCursoId(cursoBase.getIdCurso());

        assertEquals(2, egressos.size());
        assertTrue(egressos.contains(egressoCriado));
        assertTrue(egressos.contains(egressoCriado2));
        assertFalse(egressos.contains(egressoCriado3));
    }
    @Test
    public void deveGerarErroAoBuscarEgressosPorCursoIdPassandoNull() {
        assertThrows(IllegalArgumentException.class, () -> service.buscarEgressosPorCursoId(null));
    }
    @Test
    public void deveGerarErroAoBuscarEgressosPorCursoIdInexistente() {
        assertThrows(EntityNotFoundException.class, () -> service.buscarEgressosPorCursoId(100));
    }
    @Test
    @Transactional
    public void deveVincularEgressoACursoFluxoPadrao() {
        Egresso egressoCriado = criarEgressoTestes();
        VincularCursoUC.Request request = VincularCursoUC.Request.builder()
                .idCurso(cursoBase2.getIdCurso())
                .anoInicio(2020)
                .anoFim(2023)
                .build();

        service.vincularCurso(egressoCriado.getIdEgresso(), request);
        entityManager.clear();
        Optional<CursoEgresso> response = cursoEgressoJpaRepository.findById(CursoEgressoId.builder().idEgresso(egressoCriado.getIdEgresso()).idCurso(cursoBase2.getIdCurso()).build());
        assertTrue(response.isPresent());
        CursoEgresso cursoEgresso = response.get();
        assertEquals(cursoBase2.getIdCurso(), cursoEgresso.getCurso().getIdCurso());
        assertEquals(egressoCriado.getIdEgresso(), cursoEgresso.getEgresso().getIdEgresso());
    }
    @Test
    @Transactional
    public void deveVincularEgressoACursoComAnoFimNull() {
        Egresso egressoCriado = criarEgressoTestes();
        VincularCursoUC.Request request = VincularCursoUC.Request.builder()
                .idCurso(cursoBase2.getIdCurso())
                .anoInicio(2020)
                .anoFim(null)
                .build();

        assertDoesNotThrow(() -> service.vincularCurso(egressoCriado.getIdEgresso(), request));
    }
    @Test
    @Transactional
    public void deveGerarErroAoVincularEgressoEmCursoJaVinculado(){
        Egresso egressoCriado = criarEgressoTestes();
        VincularCursoUC.Request request = VincularCursoUC.Request.builder().idCurso(cursoBase.getIdCurso()).anoInicio(2020).anoFim(2023).build();
        assertThrows(IllegalArgumentException.class, () -> service.vincularCurso(egressoCriado.getIdEgresso(), request));
    }
    @Test
    public void deveGerarErroAoVincularEgressoACursoPassandoEgressoIdNull() {
        VincularCursoUC.Request request = VincularCursoUC.Request.builder()
                .idCurso(cursoBase2.getIdCurso())
                .anoInicio(2020)
                .anoFim(2023)
                .build();
        assertThrows(IllegalArgumentException.class, () -> service.vincularCurso(null, request));
    }
    @Test
    public void deveGerarErroAoVincularEgressoACursoPassandoEgressoIdInexistente() {
        VincularCursoUC.Request request = VincularCursoUC.Request.builder()
                .idCurso(cursoBase2.getIdCurso())
                .anoInicio(2020)
                .anoFim(2023)
                .build();
        assertThrows(EntityNotFoundException.class, () -> service.vincularCurso(100, request));
    }
    @Test
    @Transactional
    public void deveGerarErroAoVincularEgressoACursoAnoInicioMaiorQueAnoFim() {
        Egresso egressoCriado = criarEgressoTestes();
        VincularCursoUC.Request request = VincularCursoUC.Request.builder()
                .idCurso(cursoBase2.getIdCurso())
                .anoInicio(2024)
                .anoFim(2023)
                .build();
        assertThrows(IllegalArgumentException.class, () -> service.vincularCurso(egressoCriado.getIdEgresso(), request));
    }
    @Test
    @Transactional
    public void deveGerarErroAoVincularEgressoACursoCursoIdNull(){
        Egresso egressoCriado = criarEgressoTestes();
        VincularCursoUC.Request request = VincularCursoUC.Request.builder()
                .idCurso(null)
                .anoInicio(2020)
                .anoFim(2023)
                .build();
        assertThrows(IllegalArgumentException.class, () -> service.vincularCurso(egressoCriado.getIdEgresso(), request));
    }
    @Test
    @Transactional
    public void deveGerarErroAoVincularEgressoACursoCursoIdInexistente(){
        Egresso egressoCriado = criarEgressoTestes();
        VincularCursoUC.Request request = VincularCursoUC.Request.builder()
                .idCurso(100)
                .anoInicio(2020)
                .anoFim(2023)
                .build();
        assertThrows(EntityNotFoundException.class, () -> service.vincularCurso(egressoCriado.getIdEgresso(), request));
    }
    @Test
    @Transactional
    public void devePoderVincularEgressoAMaisDeUmCurso(){
        Egresso egressoCriado = criarEgressoTestes();
        VincularCursoUC.Request request = VincularCursoUC.Request.builder()
                .idCurso(cursoBase2.getIdCurso())
                .anoInicio(2020)
                .anoFim(2023)
                .build();

        service.vincularCurso(egressoCriado.getIdEgresso(), request);
        entityManager.clear();
        Egresso egresso = service.buscarEgressoPorId(egressoCriado.getIdEgresso());
        assertEquals(2, egresso.getCursoEgressos().size());
    }

    private Egresso criarEgressoTestes() {
        SalvarEgressoUC.Request egresso = SalvarEgressoUC.Request.builder()
                .nome("Igor")
                .email("QYv3I@example.com")
                .descricao("Descrição")
                .foto("Foto")
                .linkedin("Linkedin")
                .instagram("Instagram")
                .curriculo("Curriculo")
                .idCurso(cursoBase.getIdCurso())
                .anoInicioCurso(2020)
                .anoFimCurso(2023)
                .build();
        return service.salvarEgresso(egresso);
    }
}
