package com.ufma.portalegresso.application.out;

public interface SenhaEncoder {
    String encode(String senha);
    boolean checkSenha(String senha, String senhaCriptografada);
}
