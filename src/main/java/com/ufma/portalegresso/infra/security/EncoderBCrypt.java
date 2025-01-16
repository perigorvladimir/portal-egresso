package com.ufma.portalegresso.infra.security;

import com.ufma.portalegresso.application.out.SenhaEncoder;

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
