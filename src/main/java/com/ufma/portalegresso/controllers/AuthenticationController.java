package com.ufma.portalegresso.controllers;

import com.ufma.portalegresso.application.domain.Coordenador;
import com.ufma.portalegresso.application.out.CoordenadorJpaRepository;
import com.ufma.portalegresso.infra.security.EncoderDinamico;
import com.ufma.portalegresso.infra.security.TokenService;
import jakarta.validation.Valid;
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
    private final CoordenadorJpaRepository coordenadorJpaRepository;
    private final EncoderDinamico encoderDinamico;
    private final TokenService tokenService;
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid AuthenticationDTO request) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(request.login(), request.senha());
        var auth = this.authManager.authenticate(usernamePassword);

        var token = tokenService.generateToken((Coordenador) auth.getPrincipal());
        System.out.println(auth.getName());
        System.out.println(auth.getPrincipal());
        var admin = coordenadorJpaRepository.findByLogin(request.login());
        return ResponseEntity.ok(new LoginResponseDTO(token));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid AuthenticationDTO request) {
        if(coordenadorJpaRepository.existsByLogin(request.login())) {
            return ResponseEntity.badRequest().build();
        }
        String senhaCriptografada = encoderDinamico.encode(request.senha(), "bcrypt");
        System.out.println(senhaCriptografada);
        Coordenador coord =  Coordenador.builder()
                .login(request.login())
                .senha(senhaCriptografada)
                .nome("asjdskfshsdkjdfjhsk")
                .build();
        coordenadorJpaRepository.save(coord);
        return ResponseEntity.ok().build();
    }

    public record AuthenticationDTO(String login, String senha) {}

    public record LoginResponseDTO(String token) {}
}
