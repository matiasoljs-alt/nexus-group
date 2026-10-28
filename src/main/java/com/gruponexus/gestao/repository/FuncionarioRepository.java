package com.gruponexus.gestao.repository;

import com.gruponexus.gestao.entity.Funcionario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FuncionarioRepository extends JpaRepository<Funcionario, Long> {
    Optional<Funcionario> findByMatricula(String matricula);
    Optional<Funcionario> findByEmail(String email);
    Optional<Funcionario> findByUsuarioId(Long usuarioId);
    Optional<Funcionario> findByUsuarioUsername(String username);
    
    boolean existsByMatricula(String matricula);
    boolean existsByEmail(String email);
    boolean existsByCpf(String cpf);

    long countByStatus(String status);
    long countBySetorId(Long setorId);

    @Query("SELECT f.setor.nome, COUNT(f) FROM Funcionario f WHERE f.status = 'ATIVO' GROUP BY f.setor.nome")
    List<Object[]> countActiveFuncionariosBySetor();

    @Query("SELECT f FROM Funcionario f WHERE " +
           "(:busca IS NULL OR LOWER(f.nome) LIKE LOWER(CONCAT('%', :busca, '%')) OR " +
           " LOWER(f.matricula) LIKE LOWER(CONCAT('%', :busca, '%')) OR " +
           " LOWER(f.email) LIKE LOWER(CONCAT('%', :busca, '%'))) AND " +
           "(:setorId IS NULL OR f.setor.id = :setorId) AND " +
           "(:cargoId IS NULL OR f.cargo.id = :cargoId) AND " +
           "(:status IS NULL OR f.status = :status) " +
           "ORDER BY f.nome ASC")
    List<Funcionario> buscarComFiltros(
            @Param("busca") String busca,
            @Param("setorId") Long setorId,
            @Param("cargoId") Long cargoId,
            @Param("status") String status
    );
}
