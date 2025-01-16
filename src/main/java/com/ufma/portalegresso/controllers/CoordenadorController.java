package com.ufma.portalegresso.controllers;

import com.ufma.portalegresso.application.out.SenhaEncoder;
import com.ufma.portalegresso.application.usecases.coordenador.CoordenadorUC;
import com.ufma.portalegresso.application.usecases.coordenador.SalvarCoordenadorUC;
import com.ufma.portalegresso.shared.ResponseApi;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/coordenador")
@AllArgsConstructor
public class CoordenadorController {
    private final CoordenadorUC coordenadorUC;
    private final SenhaEncoder senhaEncoder;
    @GetMapping
    public ResponseEntity<?> buscarTodosCoordenadores() {
        var coordenadores = coordenadorUC.buscarTodosCoordenadores();
        return ResponseEntity.ok(ResponseApi.builder().dado(coordenadores).status(200).build());
    }
    @PostMapping
    public ResponseEntity<?> salvarCoordenador(@RequestBody SalvarCoordenadorUC.Request request) {
        var coordenadorSalvo = coordenadorUC.salvarCoordenador(request, senhaEncoder);
        return ResponseEntity.ok(ResponseApi.builder().dado(coordenadorSalvo).status(200).build());
    }
}
