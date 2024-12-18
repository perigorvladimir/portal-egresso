package com.ufma.portalegressos.application.usecases.egresso;

import com.ufma.portalegressos.application.domain.Egresso;

public interface SalvarEgressoUC {
    Egresso salvarEgresso(Request request);

    class Request{
        String nome;
        String email;
        String descricao;
        String foto;
        String linkedin;
        String Instagram;
        String curriculo;
    }
}
