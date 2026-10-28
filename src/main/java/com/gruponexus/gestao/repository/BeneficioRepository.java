package com.gruponexus.gestao.repository;

import com.gruponexus.gestao.entity.Beneficio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BeneficioRepository extends JpaRepository<Beneficio, Long> {
    List<Beneficio> findByAtivoTrue();
    List<Beneficio> findByCategoriaAndAtivoTrue(String categoria);
}
