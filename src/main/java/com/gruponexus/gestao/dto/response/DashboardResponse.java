package com.gruponexus.gestao.dto.response;

import com.gruponexus.gestao.entity.Comunicado;
import com.gruponexus.gestao.entity.Escala;

import java.util.List;
import java.util.Map;

public class DashboardResponse {

    private long totalFuncionarios;
    private long funcionariosAtivos;
    private long funcionariosInativos;
    private long totalSetores;
    private long totalCargos;
    private long totalBeneficios;
    private List<Map<String, Object>> funcionariosPorSetor;
    private List<Comunicado> comunicadosRecentes;
    private Escala escalaAtual; // para o colaborador ou visão geral
    private Map<String, Object> resumoPerfil; // quando funcionário logado

    public DashboardResponse() {}

    public long getTotalFuncionarios() {
        return totalFuncionarios;
    }

    public void setTotalFuncionarios(long totalFuncionarios) {
        this.totalFuncionarios = totalFuncionarios;
    }

    public long getFuncionariosAtivos() {
        return funcionariosAtivos;
    }

    public void setFuncionariosAtivos(long funcionariosAtivos) {
        this.funcionariosAtivos = funcionariosAtivos;
    }

    public long getFuncionariosInativos() {
        return funcionariosInativos;
    }

    public void setFuncionariosInativos(long funcionariosInativos) {
        this.funcionariosInativos = funcionariosInativos;
    }

    public long getTotalSetores() {
        return totalSetores;
    }

    public void setTotalSetores(long totalSetores) {
        this.totalSetores = totalSetores;
    }

    public long getTotalCargos() {
        return totalCargos;
    }

    public void setTotalCargos(long totalCargos) {
        this.totalCargos = totalCargos;
    }

    public long getTotalBeneficios() {
        return totalBeneficios;
    }

    public void setTotalBeneficios(long totalBeneficios) {
        this.totalBeneficios = totalBeneficios;
    }

    public List<Map<String, Object>> getFuncionariosPorSetor() {
        return funcionariosPorSetor;
    }

    public void setFuncionariosPorSetor(List<Map<String, Object>> funcionariosPorSetor) {
        this.funcionariosPorSetor = funcionariosPorSetor;
    }

    public List<Comunicado> getComunicadosRecentes() {
        return comunicadosRecentes;
    }

    public void setComunicadosRecentes(List<Comunicado> comunicadosRecentes) {
        this.comunicadosRecentes = comunicadosRecentes;
    }

    public Escala getEscalaAtual() {
        return escalaAtual;
    }

    public void setEscalaAtual(Escala escalaAtual) {
        this.escalaAtual = escalaAtual;
    }

    public Map<String, Object> getResumoPerfil() {
        return resumoPerfil;
    }

    public void setResumoPerfil(Map<String, Object> resumoPerfil) {
        this.resumoPerfil = resumoPerfil;
    }
}
