package com.ufma.portalegresso.controllers;

import com.ufma.portalegresso.application.out.SenhaEncoder;
import com.ufma.portalegresso.application.usecases.coordenador.CoordenadorUC;
import com.ufma.portalegresso.application.usecases.coordenador.SalvarCoordenadorUC;
import com.ufma.portalegresso.application.usecases.coordenador.UpdateCoordenadorUC;
import com.ufma.portalegresso.shared.ResponseApi;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

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
    @GetMapping("/{idCoordenador}")
    public ResponseEntity<?> buscarCoordenadorPorId(@PathVariable Integer idCoordenador) {
        var coordenador = coordenadorUC.buscarCoordenadorPorId(idCoordenador);
        return ResponseEntity.ok(ResponseApi.builder().dado(coordenador).status(200).build());
    }
    @PostMapping
    @Transactional
    public ResponseEntity<?> salvarCoordenador(@Valid @RequestBody SalvarCoordenadorUC.Request request) {
        var coordenadorSalvo = coordenadorUC.salvarCoordenador(request, senhaEncoder);
        URI location = URI.create("/coordenador/" + coordenadorSalvo.getIdCoordenador());
        return ResponseEntity.created(location).body(ResponseApi.builder().dado(coordenadorSalvo).status(201).build());
    }

    @PutMapping("/{idCoordenador}")
    @Transactional
    public ResponseEntity<?> atualizarCoordenador(@PathVariable Integer idCoordenador, @Valid @RequestBody UpdateCoordenadorUC.Request request) {
        var coordenadorSalvo = coordenadorUC.updateCoordenador(idCoordenador, request);
        return ResponseEntity.ok(ResponseApi.builder().dado(coordenadorSalvo).status(200).build());
    }

    @DeleteMapping("/{idCoordenador}")
    @Transactional
    public ResponseEntity<?> deletarCoordenador(@PathVariable Integer idCoordenador) {
        coordenadorUC.deletarCoordenadorPorId(idCoordenador);

        return ResponseEntity.ok().body(
                ResponseApi.builder()
                        .mensagem("Coordenador deletado com sucesso.")
                        .status(HttpStatus.OK.value())
                        .build()
        );
    }
}
