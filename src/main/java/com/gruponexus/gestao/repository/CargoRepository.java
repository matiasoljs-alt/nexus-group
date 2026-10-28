package com.gruponexus.gestao.repository;

import com.gruponexus.gestao.entity.Cargo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CargoRepository extends JpaRepository<Cargo, Long> {
    List<Cargo> findByAtivoTrue();
    List<Cargo> findBySetorId(Long setorId);
    List<Cargo> findBySetorIdAndAtivoTrue(Long setorId);
}
