package com.ufma.portalegresso.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ufma.portalegresso.application.domain.Egresso;
import com.ufma.portalegresso.application.services.AuthorizationService;
import com.ufma.portalegresso.application.usecases.egresso.EgressoUC;
import com.ufma.portalegresso.application.usecases.egresso.SalvarEgressoUC;
import com.ufma.portalegresso.application.usecases.egresso.UpdateEgressoUC;
import com.ufma.portalegresso.infra.TestSecurityConfig;
import com.ufma.portalegresso.infra.security.TokenService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
@WebMvcTest(controllers = EgressoController.class)
@AutoConfigureMockMvc
@Import(TestSecurityConfig.class)
public class EgressoControllerTest {
    static final String api = "/egresso";
    @Autowired
    private MockMvc mvc;
    @MockBean
    private EgressoUC egressoUC;
    @MockBean
    private TokenService tokenService;
    @MockBean
    private AuthorizationService authorizationService;

    @Test
    public void deveBuscarTodosEgressos() throws Exception {
        Egresso egresso1 = Egresso.builder()
                .nome("Egresso 1")
                .email("egresso1@ufma")
                .build();
        Egresso egresso2 = Egresso.builder()
                .nome("Egresso 2")
                .email("egresso2@ufma")
                .build();

        Mockito.when(egressoUC.buscarTodosEgressos()).thenReturn(List.of(egresso1, egresso2));

        mvc.perform(MockMvcRequestBuilders.get(api))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void deveBuscarEgressoPorId() throws Exception {
        Integer id = 1;
        Egresso egresso = Egresso.builder()
                .nome("Egresso 1")
                .email("egresso1@ufma")
                .build();

        Mockito.when(egressoUC.buscarEgressoPorId(Mockito.any(Integer.class))).thenReturn(egresso);

        String json = new ObjectMapper().writeValueAsString(id);

        mvc.perform(MockMvcRequestBuilders.get(api + "/{id}", id))
                .andExpect(MockMvcResultMatchers.status().isOk());

        Mockito.verify(egressoUC, Mockito.times(1)).buscarEgressoPorId(id);
        Mockito.verifyNoMoreInteractions(egressoUC);
    }

    @Test
    public void deveDeletarEgresso() throws Exception {
        Integer id = 1;

        Mockito.doNothing().when(egressoUC).deletarEgressoPorId(Mockito.any(Integer.class));

        mvc.perform(MockMvcRequestBuilders.delete(api + "/{id}", id))
                .andExpect(MockMvcResultMatchers.status().isOk());

        Mockito.verify(egressoUC, Mockito.times(1)).deletarEgressoPorId(id);
        Mockito.verifyNoMoreInteractions(egressoUC);
    }
    @Test
    public void deveAtualizarEgresso() throws Exception {
        Integer id = 1;
        UpdateEgressoUC.Request request =  UpdateEgressoUC.Request.builder()
                .nome("Egresso 1")
                .email("egresso1@ufma")
                .curriculo("Curriculo")
                .descricao("Descricao")
                .build();
        Egresso egresso = Egresso.builder()
                .nome("Egresso 1")
                .email("egresso1@ufma")
                .curriculo("Curriculo")
                .descricao("Descricao")
                .build();

        Mockito.when(egressoUC.updateEgresso(Mockito.any(Integer.class), Mockito.any(UpdateEgressoUC.Request.class))).thenReturn(egresso);

        String json = new ObjectMapper().writeValueAsString(request);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put(api + "/{id}", id)
                .accept("application/json")
                .contentType("application/json")
                .content(json);

        mvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void deveSalvarEgresso() throws Exception {
        SalvarEgressoUC.Request request =  SalvarEgressoUC.Request.builder()
                .nome("Egresso 1")
                .email("egresso1@ufma")
                .curriculo("Curriculo")
                .descricao("Descricao")
                .idCurso(1)
                .anoInicioCurso(2019)
                .anoFimCurso(2023)
                .build();
        Egresso egresso = Egresso.builder()
                .nome("Egresso 1")
                .email("egresso1@ufma")
                .curriculo("Curriculo")
                .descricao("Descricao")
                .build();

        Mockito.when(egressoUC.salvarEgresso(Mockito.any(SalvarEgressoUC.Request.class))).thenReturn(egresso);

        String json = new ObjectMapper().writeValueAsString(request);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post(api)
                .accept("application/json")
                .contentType("application/json")
                .content(json);

        mvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void deveBuscarEgressoPorCurso() throws Exception {
        Integer idCurso = 1;

        Egresso egresso1 = Egresso.builder()
                .nome("Egresso 1")
                .email("egresso1@ufma")
                .build();
        Egresso egresso2 = Egresso.builder()
                .nome("Egresso 2")
                .email("egresso2@ufma")
                .build();

        Mockito.when(egressoUC.buscarEgressosPorCursoId(Mockito.any(Integer.class))).thenReturn(List.of(egresso1, egresso2));

        MockHttpServletRequestBuilder request =
                MockMvcRequestBuilders.get(api + "/porCurso/{id}", idCurso)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON);

        mvc.perform(request).andExpect(MockMvcResultMatchers.status().isOk());
    }
}
