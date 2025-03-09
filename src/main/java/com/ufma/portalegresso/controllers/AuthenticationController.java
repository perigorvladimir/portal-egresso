package com.ufma.portalegresso.controllers;

import com.ufma.portalegresso.application.domain.Coordenador;
import com.ufma.portalegresso.application.services.CoordenadorService;
import com.ufma.portalegresso.application.usecases.coordenador.CoordenadorUC;
import com.ufma.portalegresso.application.usecases.coordenador.SalvarCoordenadorUC;
import com.ufma.portalegresso.infra.security.EncoderDinamico;
import com.ufma.portalegresso.infra.security.TokenService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.authentication.AuthenticationManager;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthenticationController {
    private final AuthenticationManager authManager;
    private final TokenService tokenService;
    private final CoordenadorUC coordenadorUC;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid AuthenticationDTO request) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(request.login(), request.senha());
        var auth = this.authManager.authenticate(usernamePassword);
        var token = tokenService.generateToken((Coordenador) auth.getPrincipal());
        return ResponseEntity.ok(new LoginResponseDTO(token));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid SalvarCoordenadorUC.Request request) {
        Coordenador coord = coordenadorUC.salvarCoordenador(request, null);
        return ResponseEntity.ok(coord);
    }

    public record AuthenticationDTO(@NotBlank String login, @NotBlank String senha) {}

    public record LoginResponseDTO(String token) {}
}
