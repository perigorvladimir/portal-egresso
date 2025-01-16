package com.ufma.portalegresso.controllers;

import com.ufma.portalegresso.application.usecases.curso.CursoUC;
import com.ufma.portalegresso.shared.ResponseApi;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
