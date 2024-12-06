package com.ufma.portalegressos.database.repositories;

import com.ufma.portalegressos.database.entities.CargoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CargoJpaRepository extends JpaRepository<CargoEntity, Integer> {
}
