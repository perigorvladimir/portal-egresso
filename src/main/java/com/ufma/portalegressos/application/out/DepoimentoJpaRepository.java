package com.ufma.portalegressos.application.out;

import com.ufma.portalegressos.application.domain.Depoimento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DepoimentoJpaRepository extends JpaRepository<Depoimento, Integer> {
    List<Depoimento> findByDataYear(Integer ano);
    List<Depoimento> findByDataAfter(LocalDate data);
}
