package com.ufma.portalegressos.application.out;

public interface SenhaEncoder {
    String encode(String senha);
    boolean checkSenha(String senha, String senhaCriptografada);
}
