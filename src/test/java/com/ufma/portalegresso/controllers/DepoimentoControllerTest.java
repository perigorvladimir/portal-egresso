package com.ufma.portalegresso.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ufma.portalegresso.application.domain.Depoimento;
import com.ufma.portalegresso.application.services.AuthorizationService;
import com.ufma.portalegresso.application.services.CoordenadorService;
import com.ufma.portalegresso.application.usecases.depoimento.DepoimentoUC;
import com.ufma.portalegresso.application.usecases.depoimento.SalvarDepoimentoUC;
import com.ufma.portalegresso.application.usecases.depoimento.UpdateDepoimentoUC;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.util.List;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
@WebMvcTest(controllers = DepoimentoController.class)
@AutoConfigureMockMvc
@Import(TestSecurityConfig.class)
public class DepoimentoControllerTest {
    static final String api = "/depoimento";
    @Autowired
    private MockMvc mvc;
    @MockBean
    private DepoimentoUC depoimentoUC;
    @MockBean
    private TokenService tokenService;
    @MockBean
    private AuthorizationService coordenadorService;

    @Test
    public void deveBuscarDepoimentoPorId() throws Exception {
        Integer id = 1;
        Depoimento depoimento = Depoimento.builder()
                .texto("texto depoimento")
                .build();

        Mockito.when(depoimentoUC.buscarDepoimentoPorId(Mockito.any(Integer.class))).thenReturn(depoimento);

        mvc.perform(MockMvcRequestBuilders.get(api + "/{id}", id))
                .andExpect(MockMvcResultMatchers.status().isOk());

        Mockito.verify(depoimentoUC, Mockito.times(1)).buscarDepoimentoPorId(id);
        Mockito.verifyNoMoreInteractions(depoimentoUC);
    }

    @Test
    public void deveBuscarDepoimentoPorEgresso() throws Exception {
        Integer idEgresso = 1;

        Depoimento depoimento = Depoimento.builder()
                .texto("texto depoimento")
                .build();
        Depoimento depoimento2 = Depoimento.builder()
                .texto("texto depoimento 2")
                .build();

        Mockito.when(depoimentoUC.buscarDepoimentoPorEgresso(Mockito.any(Integer.class))).thenReturn(List.of(depoimento, depoimento2));

        mvc.perform(MockMvcRequestBuilders.get(api + "/{idEgresso}", idEgresso))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void deveBuscarDepoimentoPorAno() throws Exception {
        Integer ano = 2022;

        Depoimento depoimento = Depoimento.builder()
                .texto("texto depoimento")
                .build();
        Depoimento depoimento2 = Depoimento.builder()
                .texto("texto depoimento 2")
                .build();

        Mockito.when(depoimentoUC.buscarDepoimentosPorAno(Mockito.any(Integer.class))).thenReturn(List.of(depoimento, depoimento2));

        mvc.perform(MockMvcRequestBuilders.get(api + "/porAno/{ano}", ano))
                .andExpect(MockMvcResultMatchers.status().isOk());

        Mockito.verify(depoimentoUC, Mockito.times(1)).buscarDepoimentosPorAno(ano);
        Mockito.verifyNoMoreInteractions(depoimentoUC);
    }

    @Test
    public void deveBuscarDepoimentosRecentes() throws Exception {
        Depoimento depoimento = Depoimento.builder()
                .texto("texto depoimento")
                .data(LocalDate.now())
                .build();
        Depoimento depoimento2 = Depoimento.builder()
                .texto("texto depoimento 2")
                .data(LocalDate.now().minusDays(1))
                .build();

        Mockito.when(depoimentoUC.buscarDepoimentosRecentes()).thenReturn(List.of(depoimento, depoimento2));

        mvc.perform(MockMvcRequestBuilders.get(api + "/recentes"))
                .andExpect(MockMvcResultMatchers.status().isOk());

        Mockito.verify(depoimentoUC, Mockito.times(1)).buscarDepoimentosRecentes();
        Mockito.verifyNoMoreInteractions(depoimentoUC);
    }

    @Test
    public void deveDeletarDepoimento() throws Exception {
        Integer id = 1;

        Mockito.doNothing().when(depoimentoUC).deletarPorId(Mockito.any(Integer.class));

        mvc.perform(MockMvcRequestBuilders.delete(api + "/{id}", id))
                .andExpect(MockMvcResultMatchers.status().isOk());

        Mockito.verify(depoimentoUC, Mockito.times(1)).deletarPorId(id);
        Mockito.verifyNoMoreInteractions(depoimentoUC);
    }

    @Test
    public void deveSalvarDepoimento() throws Exception {
        SalvarDepoimentoUC.Request depoimento = SalvarDepoimentoUC.Request.builder()
                .texto("depoimento padrao")
                .idEgresso(3)
                .build();
        Depoimento depoimentoSalvo = Depoimento.builder()
                .texto("depoimento padrao")
                .build();

        Mockito.when(depoimentoUC.salvarDepoimento(Mockito.any(SalvarDepoimentoUC.Request.class))).thenReturn(depoimentoSalvo);

        String json = new ObjectMapper().writeValueAsString(depoimento);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post(api)
                .accept("application/json")
                .contentType("application/json")
                .content(json);

        mvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    public void deveAtualizarDepoimento() throws Exception{
        Integer id = 1;
        UpdateDepoimentoUC.Request request = UpdateDepoimentoUC.Request.builder()
                .texto("texto basico")
                .build();
        Depoimento depoimento = Depoimento.builder()
                .idDepoimento(1)
                .texto("texto basico")
                .data(LocalDate.now())
                .build();

        Mockito.when(depoimentoUC.updateDepoimento(Mockito.any(Integer.class), Mockito.any(UpdateDepoimentoUC.Request.class))).thenReturn(depoimento);

        String json = new ObjectMapper().writeValueAsString(request);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put(api + "/{id}", id)
                .accept("application/json")
                .contentType("application/json")
                .content(json);

        mvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
