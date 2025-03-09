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
        if (algoritmo == null) { // algoritmo default
            algoritmo = "bcrypt";
        }
        //se nao tiver o algortimo informado
        if (!encoders.containsKey(algoritmo)) {
            throw new IllegalArgumentException("Algoritmo de codificação desconhecido: " + algoritmo);
        }
        PasswordEncoder encoder = encoders.get(algoritmo);
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