package com.ufma.portalegressos.application.services;

import com.ufma.portalegressos.application.domain.Coordenador;
import com.ufma.portalegressos.application.out.CoordenadorJpaRepository;
import com.ufma.portalegressos.application.out.SenhaEncoder;
import com.ufma.portalegressos.application.usecases.coordenador.CoordenadorUC;
import jakarta.validation.ConstraintViolationException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class CoordenadorService implements CoordenadorUC {
    private final CoordenadorJpaRepository coordenadorJpaRepository;
    @Override
    public Coordenador salvarCoordenador(Request request, SenhaEncoder senhaEncoder) {
        //verificar login
        if(coordenadorJpaRepository.existsByLogin(request.getLogin())){
            throw new RuntimeException("Já existe um usuário cadastrado com esse login");
        }

        String senhaCriptografada = senhaEncoder.encode(request.getSenha());

        Coordenador coord = Coordenador.builder().nome(request.getNome()).login(request.getLogin()).senha(senhaCriptografada).build();

        return coordenadorJpaRepository.save(coord);
    }
}
