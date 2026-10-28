package com.gruponexus.gestao.repository;

import com.gruponexus.gestao.entity.Escala;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface EscalaRepository extends JpaRepository<Escala, Long> {
    List<Escala> findByAtivoTrue();
}
