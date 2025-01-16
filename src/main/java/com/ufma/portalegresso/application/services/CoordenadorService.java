package com.ufma.portalegresso.application.services;

import com.ufma.portalegresso.application.domain.Coordenador;
import com.ufma.portalegresso.application.out.CoordenadorJpaRepository;
import com.ufma.portalegresso.application.out.SenhaEncoder;
import com.ufma.portalegresso.application.usecases.coordenador.CoordenadorUC;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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

    @Override
    public List<Coordenador> buscarTodosCoordenadores() {
        return coordenadorJpaRepository.findAll();
    }
}
