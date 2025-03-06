package com.ufma.portalegresso.infra;

import com.ufma.portalegresso.application.out.SenhaEncoder;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
public class SenhaEncoderFake implements SenhaEncoder {

    @Override
    public String encode(String senha, String algoritmoCriptografia) {
        return senha;
    }

    @Override
    public boolean checkSenha(String senha, String senhaCriptografada) {
        return senha.equals(senhaCriptografada);
    }
}
