package com.ufma.portalegresso.controllers;

import com.ufma.portalegresso.application.services.EgressoService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/egressos")
@AllArgsConstructor
public class EgressoController {
    private final EgressoService egressoService;
    @GetMapping
    public String buscarTodosEgressos() {
        return "ok";
    }
}
