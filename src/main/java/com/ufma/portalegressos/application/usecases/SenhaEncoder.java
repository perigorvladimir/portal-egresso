package com.ufma.portalegressos.application.usecases;

public interface SenhaEncoder {
    String hashSenha(String senha);
    boolean checkSenha(String senha, String senhaCriptografada);
}
