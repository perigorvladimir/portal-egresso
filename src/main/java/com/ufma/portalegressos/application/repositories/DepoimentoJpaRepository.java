package com.ufma.portalegressos.application.repositories;

import com.ufma.portalegressos.application.domain.Depoimento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepoimentoJpaRepository extends JpaRepository<Depoimento, Integer> {
}
