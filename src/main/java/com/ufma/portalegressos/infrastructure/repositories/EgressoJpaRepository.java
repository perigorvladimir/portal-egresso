package com.ufma.portalegressos.infrastructure.repositories;

import com.ufma.portalegressos.infrastructure.entities.EgressoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EgressoJpaRepository extends JpaRepository<EgressoEntity, Integer> {
}
