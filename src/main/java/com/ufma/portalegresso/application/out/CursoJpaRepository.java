package com.ufma.portalegresso.application.out;

import com.ufma.portalegresso.application.domain.Curso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CursoJpaRepository extends JpaRepository<Curso, Integer> {
}
