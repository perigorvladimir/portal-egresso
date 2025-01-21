package com.ufma.portalegresso.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ufma.portalegresso.application.domain.Cargo;
import com.ufma.portalegresso.application.domain.TipoAreaTrabalho;
import com.ufma.portalegresso.application.usecases.cargo.CargoUC;
import com.ufma.portalegresso.application.usecases.cargo.SalvarCargoUC;
import com.ufma.portalegresso.application.usecases.cargo.UpdateCargoUC;
import com.ufma.portalegresso.infra.TestSecurityConfig;
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

    @Test
    public void deveBuscarCargoPorId() throws Exception {
        // cenario
        Integer dto = 1;
        Cargo cargo = Cargo.builder().anoInicio(2020).descricao("descricao").anoFim(2021).local("UFMA").build();

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
        SalvarCargoUC.Request  cargo = SalvarCargoUC.Request.builder().tipoAreaTrabalho("RH").anoInicio(2020).descricao("descricao").anoFim(2021).local("UFMA").build();
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
        UpdateCargoUC.Request cargo = UpdateCargoUC.Request.builder().anoInicio(2020).descricao("descricao").anoFim(2021).local("UFMA").build();
        Cargo cargoSalvo = Cargo.builder().anoInicio(2020).descricao("descricao").anoFim(2021).local("UFMA").build();

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
