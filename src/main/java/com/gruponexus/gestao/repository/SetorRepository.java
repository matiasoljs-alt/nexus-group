package com.gruponexus.gestao.repository;

import com.gruponexus.gestao.entity.Setor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SetorRepository extends JpaRepository<Setor, Long> {
    List<Setor> findByAtivoTrue();
    boolean existsByNomeIgnoreCase(String nome);
}
