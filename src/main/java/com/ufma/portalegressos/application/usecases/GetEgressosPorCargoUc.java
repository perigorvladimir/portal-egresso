package com.ufma.portalegressos.application.usecases;

public interface GetEgressosPorCargoUc {
    public List<Egresso> execute(TipoCargo tipoCargo);
}
