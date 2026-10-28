package com.gruponexus.gestao.service;

import com.gruponexus.gestao.dto.response.DashboardResponse;
import com.gruponexus.gestao.entity.*;
import com.gruponexus.gestao.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class DashboardService {

    private final FuncionarioRepository funcionarioRepository;
    private final SetorRepository setorRepository;
    private final CargoRepository cargoRepository;
    private final BeneficioRepository beneficioRepository;
    private final ComunicadoRepository comunicadoRepository;

    public DashboardService(FuncionarioRepository funcionarioRepository,
                            SetorRepository setorRepository,
                            CargoRepository cargoRepository,
                            BeneficioRepository beneficioRepository,
                            ComunicadoRepository comunicadoRepository) {
        this.funcionarioRepository = funcionarioRepository;
        this.setorRepository = setorRepository;
        this.cargoRepository = cargoRepository;
        this.beneficioRepository = beneficioRepository;
        this.comunicadoRepository = comunicadoRepository;
    }

    @Transactional(readOnly = true)
    public DashboardResponse obterMetricas(Usuario usuarioLogado) {
        DashboardResponse dto = new DashboardResponse();

        long total = funcionarioRepository.count();
        long ativos = funcionarioRepository.countByStatus("ATIVO");
        long inativos = total - ativos;

        dto.setTotalFuncionarios(total);
        dto.setFuncionariosAtivos(ativos);
        dto.setFuncionariosInativos(inativos);
        dto.setTotalSetores(setorRepository.count());
        dto.setTotalCargos(cargoRepository.count());
        dto.setTotalBeneficios(beneficioRepository.count());

        // Gráfico / Distribuição por setor
        List<Object[]> rawSetorCounts = funcionarioRepository.countActiveFuncionariosBySetor();
        List<Map<String, Object>> setorList = new ArrayList<>();
        for (Object[] row : rawSetorCounts) {
            Map<String, Object> item = new HashMap<>();
            item.put("setor", row[0]);
            item.put("quantidade", row[1]);
            setorList.add(item);
        }
        dto.setFuncionariosPorSetor(setorList);

        // Comunicados Recentes
        dto.setComunicadosRecentes(comunicadoRepository.findTop5ByAtivoTrueOrderByDataPublicacaoDesc());

        // Se o usuário logado possui um funcionário vinculado, preenche dados contextuais
        if (usuarioLogado != null) {
            Funcionario f = funcionarioRepository.findByUsuarioId(usuarioLogado.getId()).orElse(null);
            if (f != null) {
                dto.setEscalaAtual(f.getEscala());

                Map<String, Object> resumo = new HashMap<>();
                resumo.put("nome", f.getNome());
                resumo.put("matricula", f.getMatricula());
                resumo.put("cargo", f.getCargo() != null ? f.getCargo().getNome() : "Não definido");
                resumo.put("setor", f.getSetor() != null ? f.getSetor().getNome() : "Não definido");
                resumo.put("totalBeneficios", f.getBeneficios().size());
                dto.setResumoPerfil(resumo);
            }
        }

        return dto;
    }
}
