package com.ufma.portalegressos.infrastructure.repositories;

import com.ufma.portalegressos.application.domain.Egresso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EgressoJpaRepository extends JpaRepository<Egresso, Integer> {
}
