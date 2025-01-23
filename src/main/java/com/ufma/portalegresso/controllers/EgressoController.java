package com.ufma.portalegresso.controllers;

import com.ufma.portalegresso.application.usecases.egresso.EgressoUC;
import com.ufma.portalegresso.application.usecases.egresso.VincularCursoUC;
import com.ufma.portalegresso.application.usecases.egresso.SalvarEgressoUC;
import com.ufma.portalegresso.application.usecases.egresso.UpdateEgressoUC;
import com.ufma.portalegresso.shared.ResponseApi;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/egresso")
@AllArgsConstructor
public class EgressoController {
    private final EgressoUC egressoUC;
    @GetMapping
    public ResponseEntity<?> buscarTodosEgressos() {
        var egressos = egressoUC.buscarTodosEgressos();
        return ResponseEntity.ok(ResponseApi.builder()
                .dado(egressos)
                .mensagem("Lista de todos os depoimentos recuperada com sucesso.")
                .status(200)
                .build());
    }
    @GetMapping("{id}")
    public ResponseEntity<?> buscarEgressoPorId(@PathVariable Integer id) {
        var egresso = egressoUC.buscarEgressoPorId(id);
        return ResponseEntity.ok(ResponseApi.builder()
                .dado(egresso)
                .mensagem("Egresso recuperado com sucesso.")
                .status(200)
                .build());
    }
    @GetMapping("/porCurso/{idCurso}")
    public ResponseEntity<?> buscarEgressosPorCurso(@PathVariable Integer idCurso) {
        var egressos = egressoUC.buscarEgressosPorCursoId(idCurso);
        return ResponseEntity.ok(ResponseApi.builder()
                .dado(egressos)
                .mensagem("Egressos recuperados com sucesso.")
                .status(200)
                .build());
    }
    @PostMapping
    @Transactional
    public ResponseEntity<?> salvarEgresso(@Valid @RequestBody SalvarEgressoUC.Request request) {
        var egresso = egressoUC.salvarEgresso(request);
        return ResponseEntity.ok(ResponseApi.builder()
                .dado(egresso)
                .mensagem("Egresso salvo com sucesso.")
                .status(200)
                .build());
    }
    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<?> deletarEgresso(@PathVariable Integer id) {
        egressoUC.deletarEgressoPorId(id);
        return ResponseEntity.ok(ResponseApi.builder()
                .mensagem("Egresso deletado com sucesso.")
                .status(200)
                .build());
    }
    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<?> atualizarEgresso(@PathVariable Integer id, @Valid @RequestBody UpdateEgressoUC.Request request) {
        var egresso = egressoUC.updateEgresso(id, request);
        return ResponseEntity.ok(ResponseApi.builder()
                .dado(egresso)
                .mensagem("Egresso atualizado com sucesso.")
                .status(200)
                .build());
    }
    @PostMapping("/{id}/curso")
    @Transactional
    public ResponseEntity<?> vincularEgressoACurso(@PathVariable Integer id, @Valid @RequestBody VincularCursoUC.Request request) {
        var response = egressoUC.vincularCurso(id, request);
        return ResponseEntity.ok(ResponseApi.builder()
                .dado(response)
                .mensagem(String.format("Egresso %s vinculado ao curso %s com sucesso.", response.getNomeEgressoVinculado(), response.getNomeCurso())) // TODO: melhorar mensagemCurso adicionado com sucesso.")
                .status(200)
                .build());
    }
}
