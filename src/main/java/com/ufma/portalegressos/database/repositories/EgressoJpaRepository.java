package com.ufma.portalegressos.database.repositories;

import com.ufma.portalegressos.database.entities.EgressoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EgressoJpaRepository extends JpaRepository<EgressoEntity, Integer> {
}
