package com.ufma.portalegresso.application.domain;

public enum TipoAreaTrabalho {
    FINANCEIRO,
    EDUCACAO,
    TECNOLOGIA,
    SAUDE,
    RH,
    VENDAS,
    MARKETING;

    public static TipoAreaTrabalho buscarPorNome(String valor) {
        if(valor == null) return null;
        return valueOf(valor);
    }
}
