package com.ufma.portalegresso.application.out;

import com.ufma.portalegresso.application.domain.Depoimento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DepoimentoJpaRepository extends JpaRepository<Depoimento, Integer> {
    @Query("SELECT d FROM Depoimento d WHERE YEAR(d.data) = :ano")
    List<Depoimento> findByDataYear(@Param("ano") Integer ano);
    List<Depoimento> findByDataAfter(LocalDate data);
}
