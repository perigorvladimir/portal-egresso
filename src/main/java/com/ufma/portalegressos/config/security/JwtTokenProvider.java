package com.ufma.portalegressos.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long validityInMilliseconds; // Exemplo: 1h

    // Geração de chave segura usando HMAC
    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes()); // Geração moderna de chave
    }

    // Geração do token JWT
    public String generateToken(String username) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()
                .claim("username", username) // Usa "claim" para armazenar o username
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256) // Configuração de assinatura
                .compact();
    }

    // Obtenção do username a partir do token
    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parser() // Usando parser() na versão 0.12.6
                .setSigningKey(getSigningKey()) // Configurando a chave para validação
                .parseClaimsJws(token) // Método correto para validação do token
                .getBody();
        return claims.get("username", String.class); // Obtendo o username do "claim"
    }

    // Validação do token JWT
    public boolean validateToken(String token) {
        try {
            Jwts.parser() // Usando o parser antigo
                    .setSigningKey(getSigningKey()) // Usando a chave para validação
                    .parseClaimsJws(token); // Valida o token
            return true;
        } catch (Exception e) {
            return false; // Se ocorrer alguma exceção, o token é inválido
        }
    }
}