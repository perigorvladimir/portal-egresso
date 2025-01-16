package com.ufma.portalegresso.controllers;

import com.ufma.portalegresso.application.services.EgressoService;
import com.ufma.portalegresso.application.usecases.egresso.EgressoUC;
import com.ufma.portalegresso.application.usecases.egresso.SalvarEgressoUC;
import com.ufma.portalegresso.shared.ResponseApi;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/egresso")
@AllArgsConstructor
public class EgressoController {
    private final EgressoUC egressoUC;
    @GetMapping
    public ResponseEntity<?> buscarTodosEgressos() {
        var egressos = egressoUC.buscarTodosEgressos();
        return ResponseEntity.ok(ResponseApi.builder().dado(egressos).status(200).build());
    }
    @PostMapping
    public ResponseEntity<?> salvarEgresso(SalvarEgressoUC.Request request) {
        var egresso = egressoUC.salvarEgresso(request);
        return ResponseEntity.ok(ResponseApi.builder().dado(egresso).status(200).build());
    }
}
