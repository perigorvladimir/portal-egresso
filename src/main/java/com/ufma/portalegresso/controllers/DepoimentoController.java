package com.ufma.portalegresso.controllers;

import com.ufma.portalegresso.application.usecases.depoimento.DepoimentoUC;
import com.ufma.portalegresso.application.usecases.depoimento.SalvarDepoimentoUC;
import com.ufma.portalegresso.application.usecases.depoimento.UpdateDepoimentoUC;
import com.ufma.portalegresso.shared.ResponseApi;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RequestMapping("/depoimento")
@RestController
@AllArgsConstructor
public class DepoimentoController {
    public final DepoimentoUC depoimentoUC;

    @GetMapping("/recentes")
    public ResponseEntity<?> buscarDepoimentosRecentes() {
        var depoimentos = depoimentoUC.buscarDepoimentosRecentes();
        return ResponseEntity.ok(ResponseApi.builder()
                .dado(depoimentos)
                .mensagem("Lista de todos os depoimentos recuperada com sucesso.")
                .status(200)
                .build());
    }

    @GetMapping("/porAno/{ano}")
    public ResponseEntity<?> buscarDepoimentosPorAno(@PathVariable Integer ano) {
        var depoimentos = depoimentoUC.buscarDepoimentosPorAno(ano);
        return ResponseEntity.ok(ResponseApi.builder()
                .dado(depoimentos)
                .mensagem("Lista de depoimentos do ano " + ano +" recuperada com sucesso.")
                .status(200)
                .build());
    }

    @GetMapping("/porEgresso/{idEgresso}")
    public ResponseEntity<?> buscarDepoimentoPorEgresso(@PathVariable Integer idEgresso) {
        var depoimentos = depoimentoUC.buscarDepoimentoPorEgresso(idEgresso);
        return ResponseEntity.ok(ResponseApi.builder()
                .dado(depoimentos)
                .mensagem("Lista de depoimentos do egresso " + idEgresso +" recuperada com sucesso.")
                .status(200)
                .build());
    }

    @GetMapping("/{idDepoimento}")
    public ResponseEntity<?> buscarDepoimentoPorId(@PathVariable Integer idDepoimento) {
        var depoimento = depoimentoUC.buscarDepoimentoPorId(idDepoimento);
        return ResponseEntity.ok(ResponseApi.builder()
                .dado(depoimento)
                .mensagem("Depoimento recuperado com sucesso.")
                .status(200)
                .build());
    }

    @PostMapping
    public ResponseEntity<?> salvarDepoimento(@RequestBody SalvarDepoimentoUC.Request request) {
        var depoimentoSalvo = depoimentoUC.salvarDepoimento(request);
        URI location = URI.create("/depoimento/" + depoimentoSalvo.getIdDepoimento());
        return ResponseEntity.created(location).body(ResponseApi.builder()
                .dado(depoimentoSalvo)
                .mensagem("Depoimento salvo com sucesso.")
                .status(201)
                .build());
    }

    @PutMapping("/{idDepoimento}")
    public ResponseEntity<?> atualizarDepoimento(@PathVariable Integer idDepoimento, @RequestBody UpdateDepoimentoUC.Request request) {
        var depoimentoSalvo = depoimentoUC.updateDepoimento(idDepoimento, request);
        return ResponseEntity.ok(ResponseApi.builder()
                .dado(depoimentoSalvo)
                .mensagem("Depoimento atualizado com sucesso.")
                .status(200)
                .build());

    }

    @DeleteMapping("/{idDepoimento}")
    public ResponseEntity<?> deletarDepoimento(@PathVariable Integer idDepoimento) {
        depoimentoUC.deletarPorId(idDepoimento);
        return ResponseEntity.ok().body(
                ResponseApi.builder()
                        .mensagem("Depoimento deletado com sucesso.")
                        .status(HttpStatus.OK.value())
                        .build()
        );
    }

}
