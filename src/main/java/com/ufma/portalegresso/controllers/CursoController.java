package com.ufma.portalegresso.controllers;

import com.ufma.portalegresso.application.usecases.curso.CursoUC;
import com.ufma.portalegresso.application.usecases.curso.SalvarCursoUC;
import com.ufma.portalegresso.shared.ResponseApi;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/curso")
@AllArgsConstructor
public class CursoController {
    private final CursoUC cursoUC;

    @GetMapping
    public ResponseEntity<?> buscarTodosCursos() {
        var cursos = cursoUC.buscarTodosCursos();
        return ResponseEntity.ok(ResponseApi.builder().dado(cursos).status(200).build());
    }

    @GetMapping("/{idCurso}")
    public ResponseEntity<?> buscarCursoPorId(@PathVariable Integer idCurso) {
        var curso = cursoUC.buscarPorId(idCurso);
        return ResponseEntity.ok(ResponseApi.builder().dado(curso).status(200).build());
    }

    @PostMapping
    public ResponseEntity<?> salvarCurso(@RequestBody SalvarCursoUC.Request request) {
        var cursoSalvo = cursoUC.salvarCurso(request);
        URI location = URI.create("/curso/" + cursoSalvo.getIdCurso());
        return ResponseEntity.created(location).body(ResponseApi.builder().dado(cursoSalvo).status(201).build());
    }

    @PutMapping("/{idCurso}/coordenador/{idCoordenador}")
    public ResponseEntity<?> designarCoordenador(@PathVariable Integer idCurso, @PathVariable Integer idCoordenador) {
        var designacao = cursoUC.designarCoordenador(idCurso, idCoordenador);
        return ResponseEntity.ok(ResponseApi.builder().dado(designacao).status(200).build());
    }

    @DeleteMapping("/{idCurso}")
    public ResponseEntity<?> deletarCurso(@PathVariable Integer idCurso) {
        cursoUC.deletarCursoPorId(idCurso);

        return ResponseEntity.ok().body(
                ResponseApi.builder()
                        .mensagem("Cargo deletado com sucesso.")
                        .status(HttpStatus.OK.value())
                        .build()
        );
    }
}
