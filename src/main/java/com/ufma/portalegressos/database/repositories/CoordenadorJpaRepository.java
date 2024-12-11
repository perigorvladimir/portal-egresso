package com.ufma.portalegressos.database.repositories;

import com.ufma.portalegressos.database.entities.CoordenadorEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CoordenadorJpaRepository extends JpaRepository<CoordenadorEntity, Integer> {
}
