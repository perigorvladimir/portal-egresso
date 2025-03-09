package com.ufma.portalegresso.application.out;

import lombok.AllArgsConstructor;
import lombok.Getter;

public interface SenhaEncoder {
    String encode(String senha, String algoritmo);
    boolean checkSenha(String senha, String senhaCriptografada);
}
