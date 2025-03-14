package com.ufma.portalegresso.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ufma.portalegresso.application.domain.Cargo;
import com.ufma.portalegresso.application.domain.TipoAreaTrabalho;
import com.ufma.portalegresso.application.services.AuthorizationService;
import com.ufma.portalegresso.application.usecases.cargo.CargoUC;
import com.ufma.portalegresso.application.usecases.cargo.SalvarCargoUC;
import com.ufma.portalegresso.application.usecases.cargo.UpdateCargoUC;
import com.ufma.portalegresso.infra.TestSecurityConfig;
import com.ufma.portalegresso.infra.security.TokenService;
import org.hamcrest.Matchers;
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
@WebMvcTest(controllers = CargoController.class)
@AutoConfigureMockMvc
@Import(TestSecurityConfig.class)
//TODO FAZER TESTES COM RELACAO AO ENUM NOVO, TIPOAREA
public class CargoControllerTest {
    static final String api = "/cargo";
    @Autowired
    private MockMvc mvc;
    @MockBean
    private CargoUC cargoUC;
    @MockBean
    private TokenService tokenService;
    @MockBean
    private AuthorizationService authorizationService;

    @Test
    public void deveBuscarCargoPorId() throws Exception {
        // cenario
        Integer dto = 1;
        Cargo cargo = Cargo.builder().idCargo(1).anoInicio(2020).descricao("descricao").anoFim(2021).local("UFMA").build();

        Mockito.when(cargoUC.buscarPorId(Mockito.any(Integer.class))).thenReturn(cargo);

        String json = new ObjectMapper().writeValueAsString(dto);

        //constroi a requisicao
        MockHttpServletRequestBuilder request =
                MockMvcRequestBuilders.get(api + "/{id}", dto)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON);

        mvc.perform(request).andExpect(MockMvcResultMatchers.status().isOk());

        Mockito.verify(cargoUC, Mockito.times(1)).buscarPorId(1);
        Mockito.verifyNoMoreInteractions(cargoUC);
    }

    @Test
    public void deveSalvarCargo() throws Exception {
        // cenario
        SalvarCargoUC.Request  cargo = SalvarCargoUC.Request.builder().idEgresso(1).tipoAreaTrabalho("TECNOLOGIA").anoInicio(2020).descricao("descricao").anoFim(2021).local("UFMA").build();
        Cargo cargoSalvo = Cargo.builder().anoInicio(2020).descricao("descricao").anoFim(2021).local("UFMA").build();

        Mockito.when(cargoUC.salvar(Mockito.any(SalvarCargoUC.Request.class))).thenReturn(cargoSalvo);

        String json = new ObjectMapper().writeValueAsString(cargo);
        //constroi a requisicao
        MockHttpServletRequestBuilder request =
                MockMvcRequestBuilders.post(api)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json);

        mvc.perform(request).andExpect(MockMvcResultMatchers.status().isCreated());
        Mockito.verify(cargoUC, Mockito.times(1)).salvar(Mockito.any(SalvarCargoUC.Request.class));
        Mockito.verifyNoMoreInteractions(cargoUC);
    }
    @Test
    public void deveGerarErroAoSalvarCargoComDescricaoOuLocalVazio() throws Exception {
        SalvarCargoUC.Request cargoDescricaoVazio = SalvarCargoUC.Request.builder().idEgresso(1).tipoAreaTrabalho("TECNOLOGIA").anoInicio(2020).descricao("").anoFim(2021).local("UFMA").build();
        SalvarCargoUC.Request cargoLocalVazio = SalvarCargoUC.Request.builder().idEgresso(1).tipoAreaTrabalho("TECNOLOGIA").anoInicio(2020).descricao("descricao").anoFim(2021).local("").build();
        SalvarCargoUC.Request cargoLocalEDescricaoVazio = SalvarCargoUC.Request.builder().idEgresso(1).tipoAreaTrabalho("TECNOLOGIA").anoInicio(2020).descricao("").anoFim(2021).local("").build();

        Cargo cargoSalvo = Cargo.builder().anoInicio(2020).descricao("descricao").anoFim(2021).local("UFMA").build();

        Mockito.when(cargoUC.salvar(Mockito.any(SalvarCargoUC.Request.class))).thenReturn(cargoSalvo);

        String jsonDescricaoVazio = new ObjectMapper().writeValueAsString(cargoDescricaoVazio);
        String jsonLocalVazio = new ObjectMapper().writeValueAsString(cargoLocalVazio);
        String jsonDescricaoELocalVAzio = new ObjectMapper().writeValueAsString(cargoLocalEDescricaoVazio);
        //constroi a requisicao
        MockHttpServletRequestBuilder requestDescricaoVazio = MockMvcRequestBuilders.post(api).accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).content(jsonDescricaoVazio);
        MockHttpServletRequestBuilder requestLocalVazio = MockMvcRequestBuilders.post(api).accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).content(jsonLocalVazio);
        MockHttpServletRequestBuilder requestDescricaoELocalVazio = MockMvcRequestBuilders.post(api).accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).content(jsonDescricaoELocalVAzio);

        mvc.perform(requestDescricaoVazio).andExpect(MockMvcResultMatchers.status().isBadRequest())
                            .andExpect(MockMvcResultMatchers.jsonPath("$.detalhes", Matchers.hasSize(1)))
                            .andExpect(MockMvcResultMatchers.jsonPath("$.detalhes[0]", Matchers.containsString("descricao")));
        mvc.perform(requestLocalVazio).andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.detalhes", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.detalhes[0]", Matchers.containsString("local")));
        mvc.perform(requestDescricaoELocalVazio).andExpect(MockMvcResultMatchers.status().isBadRequest())
                        .andExpect(MockMvcResultMatchers.jsonPath("$.detalhes", Matchers.hasSize(2)))
                        .andExpect(MockMvcResultMatchers.jsonPath("$.detalhes", Matchers.hasItems(
                                                    Matchers.containsString("descricao"),
                                                    Matchers.containsString("local"))));

        Mockito.verify(cargoUC, Mockito.times(0)).salvar(Mockito.any(SalvarCargoUC.Request.class));
        Mockito.verifyNoMoreInteractions(cargoUC);
    }
    @Test
    public void deveGerarErroAoSalvarCargoComDescricaoOuLocalOuAnoInicioOuIdEgressoOuTipoAreaTrabalhoNull() throws Exception {
        SalvarCargoUC.Request cargo = SalvarCargoUC.Request.builder().build();

        Mockito.when(cargoUC.salvar(Mockito.any(SalvarCargoUC.Request.class))).thenReturn(null);

        String json = new ObjectMapper().writeValueAsString(cargo);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(api).accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).content(json);

        mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.detalhes", Matchers.hasSize(5)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.detalhes", Matchers.hasItems(
                        Matchers.containsString("descricao"),
                        Matchers.containsString("local"),
                        Matchers.containsString("anoInicio"),
                        Matchers.containsString("idEgresso"),
                        Matchers.containsString("tipoAreaTrabalho"))));

        Mockito.verify(cargoUC, Mockito.times(0)).salvar(Mockito.any(SalvarCargoUC.Request.class));
        Mockito.verifyNoMoreInteractions(cargoUC);
    }
    @Test
    public void deveGerarErroAoSalvarComTipoAreaTrabalhoInexistente() throws Exception{
        SalvarCargoUC.Request cargoAreaInexistente = SalvarCargoUC.Request.builder().idEgresso(1).tipoAreaTrabalho("bola").descricao("descricao").anoInicio(2020).anoFim(2021).local("UFMA").build();

        Mockito.when(cargoUC.salvar(Mockito.any(SalvarCargoUC.Request.class))).thenReturn(null);

        String json = new ObjectMapper().writeValueAsString(cargoAreaInexistente);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(api).accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).content(json);

        mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.detalhes", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.detalhes[0]", Matchers.containsString("tipoAreaTrabalho")));

        Mockito.verify(cargoUC, Mockito.times(0)).salvar(Mockito.any(SalvarCargoUC.Request.class));
        Mockito.verifyNoMoreInteractions(cargoUC);
    }
    @Test
    public void deveGerarErroAoSalvarComAnoInicioEAnoFimInvalidos() throws Exception{
        SalvarCargoUC.Request cargo = SalvarCargoUC.Request.builder().idEgresso(1).tipoAreaTrabalho("FINANCEIRO").descricao("descricao").anoInicio(1890).anoFim(1895).local("UFMA").build();
        SalvarCargoUC.Request cargoAnosDepoisAnoAtual = SalvarCargoUC.Request.builder().idEgresso(1).tipoAreaTrabalho("FINANCEIRO").descricao("descricao").anoInicio(2026).anoFim(2030).local("UFMA").build();

        Mockito.when(cargoUC.salvar(Mockito.any(SalvarCargoUC.Request.class))).thenReturn(null);

        String json = new ObjectMapper().writeValueAsString(cargo);
        String jsonAnosDepoisAnoAtual = new ObjectMapper().writeValueAsString(cargoAnosDepoisAnoAtual);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(api).accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).content(json);
        MockHttpServletRequestBuilder request2 = MockMvcRequestBuilders.post(api).accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).content(jsonAnosDepoisAnoAtual);

        mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.detalhes", Matchers.hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.detalhes", Matchers.hasItems(
                        Matchers.containsString("anoInicio"),
                        Matchers.containsString("anoFim"))));
        mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.detalhes", Matchers.hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.detalhes", Matchers.hasItems(
                        Matchers.containsString("anoInicio"),
                        Matchers.containsString("anoFim"))));

        Mockito.verify(cargoUC, Mockito.times(0)).salvar(Mockito.any(SalvarCargoUC.Request.class));
        Mockito.verifyNoMoreInteractions(cargoUC);
    }
    @Test
    public void deveDeletarCargo() throws Exception {
        // cenario
        Integer dto = 1;
        Cargo cargo = Cargo.builder().anoInicio(2020).descricao("descricao").tipoAreaTrabalho(TipoAreaTrabalho.RH).anoFim(2021).local("UFMA").build();

        Mockito.doNothing().when(cargoUC).deletarPorId(Mockito.any(Integer.class));

        //constroi a requisicao
        MockHttpServletRequestBuilder request =
                MockMvcRequestBuilders.delete(api + "/{id}", dto)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON);

        mvc.perform(request).andExpect(MockMvcResultMatchers.status().isOk());

        Mockito.verify(cargoUC, Mockito.times(1)).deletarPorId(1);
        Mockito.verifyNoMoreInteractions(cargoUC);
    }
    @Test
    public void deveAtualizarCargo() throws Exception {
        // cenario
        Integer idCargo = 1;
        UpdateCargoUC.Request cargo = UpdateCargoUC.Request.builder().anoInicio(2020).tipoAreaTrabalho("RH").descricao("descricao").anoFim(2021).local("UFMA").build();
        Cargo cargoSalvo = Cargo.builder().anoInicio(2020).tipoAreaTrabalho(TipoAreaTrabalho.RH).descricao("descricao").anoFim(2021).local("UFMA").build();

        Mockito.when(cargoUC.updateCargo(Mockito.any(Integer.class), Mockito.any(UpdateCargoUC.Request.class))).thenReturn(cargoSalvo);

        String json = new ObjectMapper().writeValueAsString(cargo);

        //constroi a requisicao
        MockHttpServletRequestBuilder request =
                MockMvcRequestBuilders.put(api + "/{id}", idCargo)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json);

        mvc.perform(request).andExpect(MockMvcResultMatchers.status().isOk());

    }
    @Test
    public void deveBuscarTodosCargos() throws Exception {
        // cenario
        Cargo cargo1 = Cargo.builder().anoInicio(2020).descricao("descricao").anoFim(2021).local("UFMA").build();
        Cargo cargo2 = Cargo.builder().anoInicio(2020).descricao("descricao").anoFim(2021).local("UFMA").build();

        Mockito.when(cargoUC.buscarTodosCargos()).thenReturn(List.of(cargo1, cargo2));

        //constroi a requisicao
        MockHttpServletRequestBuilder request =
                MockMvcRequestBuilders.get(api)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON);

        mvc.perform(request).andExpect(MockMvcResultMatchers.status().isOk());

        Mockito.verify(cargoUC, Mockito.times(1)).buscarTodosCargos();
        Mockito.verifyNoMoreInteractions(cargoUC);
    }
}
