package com.ufma.portalegressos.application.out;

import com.ufma.portalegressos.application.domain.Cargo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CargoJpaRepository extends JpaRepository<Cargo, Integer> {
}
