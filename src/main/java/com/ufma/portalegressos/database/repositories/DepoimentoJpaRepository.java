package com.ufma.portalegressos.database.repositories;

import com.ufma.portalegressos.database.entities.DepoimentoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepoimentoJpaRepository extends JpaRepository<DepoimentoEntity, Integer> {
}
