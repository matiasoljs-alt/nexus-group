package com.gruponexus.gestao.repository;

import com.gruponexus.gestao.entity.Comunicado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ComunicadoRepository extends JpaRepository<Comunicado, Long> {
    List<Comunicado> findByAtivoTrueOrderByDataPublicacaoDesc();
    List<Comunicado> findTop5ByAtivoTrueOrderByDataPublicacaoDesc();
    List<Comunicado> findByAtivoTrueAndImportanteTrueOrderByDataPublicacaoDesc();
}
