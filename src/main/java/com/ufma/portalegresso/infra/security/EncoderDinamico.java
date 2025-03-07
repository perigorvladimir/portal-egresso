package com.ufma.portalegresso.infra.security;

import com.ufma.portalegresso.application.out.SenhaEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ActiveProfiles;

import java.util.HashMap;
import java.util.Map;

@Component
public class EncoderDinamico implements SenhaEncoder {
    private final PasswordEncoder delegatingEncoder;
    private final Map<String, PasswordEncoder> encoders = new HashMap<>();

    public EncoderDinamico() {
        encoders.put("bcrypt", new BCryptPasswordEncoder());
        encoders.put("noop", NoOpPasswordEncoder.getInstance());

        // Define o algoritmo padrão como Bcrypt
        this.delegatingEncoder = new DelegatingPasswordEncoder("bcrypt", encoders);
    }

    @Override
    public String encode(String senha, String algoritmo) {
        if(senha==null){
            return null;
        }
        PasswordEncoder encoder = encoders.getOrDefault(algoritmo, encoders.get("bcrypt"));
        return "{" + algoritmo + "}" + encoder.encode(senha); // Prefixa com o algoritmo usado
    }

    @Override
    public boolean checkSenha(String senha, String senhaCriptografada) {
        return delegatingEncoder.matches(senha, senhaCriptografada);
    }

    public PasswordEncoder getPasswordEncoder() {
        return delegatingEncoder;
    }
}