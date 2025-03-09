package com.ufma.portalegresso.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ufma.portalegresso.application.domain.Coordenador;
import com.ufma.portalegresso.application.services.CoordenadorService;
import com.ufma.portalegresso.application.usecases.coordenador.CoordenadorUC;
import com.ufma.portalegresso.application.usecases.coordenador.SalvarCoordenadorUC;
import com.ufma.portalegresso.application.usecases.coordenador.UpdateCoordenadorUC;
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
@WebMvcTest(controllers = CoordenadorController.class)
@AutoConfigureMockMvc
@Import(TestSecurityConfig.class)
public class CoordenadorControllerTest {
    static final String api = "/coordenador";
    @Autowired
    private MockMvc mvc;
    @MockBean
    private CoordenadorUC coordenadorUC;
    @MockBean
    private TokenService tokenService;
    @MockBean
    private CoordenadorService authorizationService;

    @Test
    public void deveBuscarTodosCoordenadores() throws Exception {
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

        Mockito.when(coordenadorUC.buscarTodosCoordenadores()).thenReturn(List.of(coordenador1, coordenador2));

        mvc.perform(MockMvcRequestBuilders.get(api))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
    @Test
    public void deveBuscarCoordenadorPorId() throws Exception {
        // cenario
        Integer dto = 1;
        Coordenador coordenador = Coordenador.builder()
                .login("login")
                .nome("nome1")
                .senha("123")
                .build();

        Mockito.when(coordenadorUC.buscarCoordenadorPorId(Mockito.any(Integer.class))).thenReturn(coordenador);

        String json = new ObjectMapper().writeValueAsString(dto);

        //constroi a requisicao
        MockHttpServletRequestBuilder request =
                MockMvcRequestBuilders.get(api + "/{id}", dto)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON);

        mvc.perform(request).andExpect(MockMvcResultMatchers.status().isOk());

        Mockito.verify(coordenadorUC, Mockito.times(1)).buscarCoordenadorPorId(1);
        Mockito.verifyNoMoreInteractions(coordenadorUC);
    }

    @Test
    public void deveDeletarCoordenador() throws Exception {
        // cenario
        Integer dto = 1;

        Mockito.doNothing().when(coordenadorUC).deletarCoordenadorPorId(Mockito.any(Integer.class));

        //constroi a requisicao
        MockHttpServletRequestBuilder request =
                MockMvcRequestBuilders.delete(api + "/{id}", dto)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON);

        mvc.perform(request).andExpect(MockMvcResultMatchers.status().isOk());

        Mockito.verify(coordenadorUC, Mockito.times(1)).deletarCoordenadorPorId(1);
        Mockito.verifyNoMoreInteractions(coordenadorUC);
    }

    @Test
    public void deveSalvarCoordenador() throws Exception {
        SalvarCoordenadorUC.Request coordenador = SalvarCoordenadorUC.Request
                .builder()
                .nome("nome coordenador")
                .login("login")
                .senha("12345")
                .build();

        Coordenador coordenadorSalvo = Coordenador.builder()
                .login("login")
                .senha("12345")
                .nome("nome coordenador")
                .build();


        Mockito.when(coordenadorUC.salvarCoordenador(Mockito.any(SalvarCoordenadorUC.Request.class), Mockito.any())).thenReturn(coordenadorSalvo);

        String json = new ObjectMapper().writeValueAsString(coordenador);

        //constroi a requisicao
        MockHttpServletRequestBuilder request =
                MockMvcRequestBuilders.post(api)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json);

        mvc.perform(request).andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    public void deveAtualizarCoordenador() throws Exception {
        Integer idCoordenador = 1;
        UpdateCoordenadorUC.Request coordenador = UpdateCoordenadorUC.Request
                .builder()
                .nome("nome coordenador")
                .build();
        Coordenador coordenadorSalvo = Coordenador.builder().login("login").nome("nome coordenador").senha("12345").build();

        Mockito.when(coordenadorUC.updateCoordenador(Mockito.any(Integer.class), Mockito.any(UpdateCoordenadorUC.Request.class))).thenReturn(coordenadorSalvo);

        String json = new ObjectMapper().writeValueAsString(coordenador);

        //constroi a requisicao
        MockHttpServletRequestBuilder request =
                MockMvcRequestBuilders.put(api + "/{id}", idCoordenador)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json);

        mvc.perform(request).andExpect(MockMvcResultMatchers.status().isOk());
    }
}
