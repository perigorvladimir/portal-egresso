package com.ufma.portalegresso.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ufma.portalegresso.application.domain.Curso;
import com.ufma.portalegresso.application.usecases.curso.CursoUC;
import com.ufma.portalegresso.application.usecases.curso.DesignarCoordenadorUC;
import com.ufma.portalegresso.application.usecases.curso.SalvarCursoUC;
import com.ufma.portalegresso.infra.TestSecurityConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.DeferredImportSelector;
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
@WebMvcTest(controllers = CursoController.class)
@AutoConfigureMockMvc
@Import(TestSecurityConfig.class)
public class CursoControllerTest {
    static final String api = "/curso";
    @Autowired
    private MockMvc mvc;
    @MockBean
    private CursoUC cursoUC;

    @Test
    public void deveBuscarTodosCursos() throws Exception {
        Curso curso1 = Curso.builder().nome("Curso 1").nivel("Graduação").build();
        Curso curso2 = Curso.builder().nome("Curso 2").nivel("Graduação").build();

        Mockito.when(cursoUC.buscarTodosCursos()).thenReturn(List.of(curso1, curso2));

        mvc.perform(MockMvcRequestBuilders.get(api))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void deveBuscarCursoPorId() throws Exception {
        // cenario
        Integer dto = 1;
        Curso curso = Curso.builder().nome("Curso 1").nivel("Graduação").build();

        Mockito.when(cursoUC.buscarPorId(Mockito.any(Integer.class))).thenReturn(curso);

        String json = new ObjectMapper().writeValueAsString(dto);

        //constroi a requisicao
        MockHttpServletRequestBuilder request =
                MockMvcRequestBuilders.get(api + "/{id}", dto)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON);

        mvc.perform(request).andExpect(MockMvcResultMatchers.status().isOk());

        Mockito.verify(cursoUC, Mockito.times(1)).buscarPorId(1);
        Mockito.verifyNoMoreInteractions(cursoUC);
    }

    @Test
    public void deveDeletarCurso() throws Exception {
        // cenario
        Integer dto = 1;

        Mockito.doNothing().when(cursoUC).deletarCursoPorId(Mockito.any(Integer.class));

        //constroi a requisicao
        MockHttpServletRequestBuilder request =
                MockMvcRequestBuilders.delete(api + "/{id}", dto)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON);

        mvc.perform(request).andExpect(MockMvcResultMatchers.status().isOk());

        Mockito.verify(cursoUC, Mockito.times(1)).deletarCursoPorId(1);
    }
    @Test
    public void deveSalvarCurso() throws Exception {
        // cenario
        SalvarCursoUC.Request curso = SalvarCursoUC.Request.builder().nome("Curso 1").nivel("Graduação").idCoordenador(1).build();
        Curso cursoSalvo = Curso.builder().nome("Curso 1").nivel("Graduação").build();

        Mockito.when(cursoUC.salvarCurso(Mockito.any(SalvarCursoUC.Request.class))).thenReturn(cursoSalvo);

        String json = new ObjectMapper().writeValueAsString(curso);

        //constroi a requisicao
        MockHttpServletRequestBuilder request =
                MockMvcRequestBuilders.post(api)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json);

        mvc.perform(request).andExpect(MockMvcResultMatchers.status().isCreated());
    }
    @Test
    public void deveDesignarCoordenador() throws Exception {
        // cenario
        Integer idCoord = 1;
        Integer idCurso = 1;
        DesignarCoordenadorUC.Response response = DesignarCoordenadorUC.Response.builder()
                .nomeCurso("Curso 1")
                .nomeNovoCoord("Coordenador 1")
                .build();


        Mockito.when(cursoUC.designarCoordenador(Mockito.any(Integer.class), Mockito.any(Integer.class))).thenReturn(response);

        //constroi a requisicao
        MockHttpServletRequestBuilder request =
                MockMvcRequestBuilders.put(api + "/{idCurso}/coordenador/{idCoordenador}", idCurso, idCoord)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON);

        mvc.perform(request).andExpect(MockMvcResultMatchers.status().isOk());
    }
}
