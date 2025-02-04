package com.ufma.portalegresso.infra.security;

import com.ufma.portalegresso.application.out.SenhaEncoder;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ActiveProfiles;

@Component
@ActiveProfiles("default")
@Primary
public class EncoderBCrypt implements SenhaEncoder {
    @Override
    public String encode(String senha) {
        return senha;
    }

    @Override
    public boolean checkSenha(String senha, String senhaCriptografada) {
        return senha.equals(senhaCriptografada);
    }
}
