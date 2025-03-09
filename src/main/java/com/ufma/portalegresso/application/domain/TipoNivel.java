package com.ufma.portalegresso.application.domain;

public enum TipoNivel {
    TECNOLOGO,
    GRADUACAO,
    POS_GRADUACAO,
    MESTRADO,
    DOUTORADO;

    public static TipoNivel buscarPorNome(String valor) {
        if(valor == null) return null;
        return valueOf(valor);
    }
}
