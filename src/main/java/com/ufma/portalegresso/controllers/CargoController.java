package com.ufma.portalegresso.controllers;

import com.ufma.portalegresso.application.usecases.cargo.CargoUC;
import com.ufma.portalegresso.application.usecases.cargo.SalvarCargoUC;
import com.ufma.portalegresso.shared.ResponseApi;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cargo")
@AllArgsConstructor
public class CargoController {
    private final CargoUC cargoUC;

    @GetMapping
    public ResponseEntity<?> buscarTodosCargos() {
        var cargos = cargoUC.buscarTodosCargos();
        return ResponseEntity.ok(ResponseApi.builder().dado(cargos).status(200).build());
    }
    @PostMapping
    public ResponseEntity<?> salvarCargo(@RequestBody SalvarCargoUC.Request request) {
        var cargoSalvo = cargoUC.salvar(request);
        return ResponseEntity.ok(ResponseApi.builder().dado(cargoSalvo).status(200).build());
    }
}
