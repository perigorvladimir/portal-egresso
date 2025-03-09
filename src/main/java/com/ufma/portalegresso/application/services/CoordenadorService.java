package com.ufma.portalegresso.application.services;

import com.ufma.portalegresso.application.domain.Coordenador;
import com.ufma.portalegresso.application.out.CoordenadorJpaRepository;
import com.ufma.portalegresso.application.out.SenhaEncoder;
import com.ufma.portalegresso.application.usecases.coordenador.CoordenadorUC;
import com.ufma.portalegresso.application.usecases.coordenador.SalvarCoordenadorUC;
import com.ufma.portalegresso.application.usecases.coordenador.UpdateCoordenadorUC;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class CoordenadorService implements CoordenadorUC, UserDetailsService {
    private final CoordenadorJpaRepository coordenadorJpaRepository;
    private final SenhaEncoder senhaEncoder;
    @Override
    public Coordenador salvarCoordenador(SalvarCoordenadorUC.Request request, String algoritmoCriptografia) {
        //verificar login
        if(request.getSenha() == null || request.getSenha().length() < 8){
            throw new IllegalArgumentException("A senha deve ter pelo menos 8 caracteres");
        }
        if(coordenadorJpaRepository.existsByLogin(request.getLogin())){
            throw new IllegalArgumentException("Já existe um usuário cadastrado com esse login");
        }
        String senhaCriptografada = senhaEncoder.encode(request.getSenha(), algoritmoCriptografia);

        Coordenador coord = Coordenador.builder().nome(request.getNome()).login(request.getLogin()).senha(senhaCriptografada).build();

        return coordenadorJpaRepository.save(coord);
    }

    @Override
    public List<Coordenador> buscarTodosCoordenadores() {
        return coordenadorJpaRepository.findAll();
    }

    @Override
    public Coordenador buscarCoordenadorPorId(Integer idCoordenador) {
        if(idCoordenador == null){
            throw new IllegalArgumentException("O id do coordenador nao pode ser nulo");
        }
        Coordenador coordenador = coordenadorJpaRepository.findById(idCoordenador).orElseThrow(() -> new EntityNotFoundException("Coordenador não encontrado com o id inserido"));
        return coordenador;
    }

    @Override
    public void deletarCoordenadorPorId(Integer id) {
        coordenadorJpaRepository.deleteById(id);
    }

    @Override
    public Coordenador updateCoordenador(Integer id, UpdateCoordenadorUC.Request request) {
        Coordenador coord = buscarCoordenadorPorId(id);
        coord.setNome(request.getNome());
        return coordenadorJpaRepository.save(coord);
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return coordenadorJpaRepository.findByLogin(username).orElseThrow(() -> new UsernameNotFoundException("Coordenador nao encontrado"));
    }
}
