package com.ufma.portalegresso.controllers;

import com.ufma.portalegresso.application.usecases.cargo.CargoUC;
import com.ufma.portalegresso.application.usecases.cargo.SalvarCargoUC;
import com.ufma.portalegresso.application.usecases.cargo.UpdateCargoUC;
import com.ufma.portalegresso.shared.ResponseApi;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/cargo")
@AllArgsConstructor
public class CargoController {
    private final CargoUC cargoUC;
    @GetMapping
    public ResponseEntity<?> buscarTodosCargos() {
        var cargos = cargoUC.buscarTodosCargos();
        return ResponseEntity.ok(ResponseApi.builder()
                .dado(cargos)
                .mensagem("Lista de todos os cargos recuperada com sucesso.")
                .status(200)
                .build());
    }

    @GetMapping("/{idCargo}")
    public ResponseEntity<?> buscarCargoPorId(@PathVariable Integer idCargo) {
        var cargo = cargoUC.buscarPorId(idCargo);
        return ResponseEntity.ok(ResponseApi.builder()
                .dado(cargo)
                .mensagem("Cargo recuperado com sucesso.")
                .status(200)
                .build());
    }

    @PostMapping
    @Transactional
    public ResponseEntity<?> salvarCargo(@RequestBody SalvarCargoUC.Request request) {
        var cargoSalvo = cargoUC.salvar(request);
        URI location = URI.create("/cargo/" + cargoSalvo.getIdCargo());
        return ResponseEntity.created(location).body(ResponseApi.builder()
                .dado(cargoSalvo)
                .mensagem("Cargo salvo com sucesso.")
                .status(201)
                .build());
    }

    @PutMapping("/{idCargo}")
    @Transactional
    public ResponseEntity<?> atualizarCargo(@PathVariable Integer idCargo, @RequestBody UpdateCargoUC.Request request) {
        var cargoSalvo = cargoUC.updateCargo(idCargo, request);
        return ResponseEntity.ok(ResponseApi.builder()
                .dado(cargoSalvo)
                .mensagem("Cargo atualizado com sucesso.")
                .status(200)
                .build());

    }

    @DeleteMapping("/{idCargo}")
    @Transactional
    public ResponseEntity<?> deletarCargo(@PathVariable Integer idCargo) {
        cargoUC.deletarPorId(idCargo);
        return ResponseEntity.ok().body(
                ResponseApi.builder()
                        .mensagem("Cargo deletado com sucesso.")
                        .status(HttpStatus.OK.value())
                        .build()
        );
    }
}