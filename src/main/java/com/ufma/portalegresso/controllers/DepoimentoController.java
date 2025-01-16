package com.ufma.portalegresso.controllers;

import com.ufma.portalegresso.application.usecases.depoimento.DepoimentoUC;
import com.ufma.portalegresso.shared.ResponseApi;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/depoimento")
@RestController
@AllArgsConstructor
public class DepoimentoController {
    public final DepoimentoUC depoimentoUC;

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarDepoimentoPorId(Integer id) {
        var depoimentos = depoimentoUC.buscarDepoimentoPorId(id);
        return ResponseEntity.ok(ResponseApi.builder().dado(depoimentos).status(200).build());
    }
}
