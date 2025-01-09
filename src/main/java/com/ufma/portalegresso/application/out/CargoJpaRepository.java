package com.ufma.portalegresso.application.out;

import com.ufma.portalegresso.application.domain.Cargo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CargoJpaRepository extends JpaRepository<Cargo, Integer> {
}
