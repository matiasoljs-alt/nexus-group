package com.gruponexus.gestao.dto.response;

import com.gruponexus.gestao.entity.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class FuncionarioResponse {

    private Long id;
    private String matricula;
    private String nome;
    private String email;
    private String telefone;
    private String cpf;
    private LocalDate dataNascimento;
    private LocalDate dataAdmissao;
    private String status;
    private String tipoContrato;
    private String observacoes;

    // Cargo resumido
    private Long cargoId;
    private String cargoNome;
    private String cargoNivel;
    private BigDecimal salarioBase;

    // Setor resumido
    private Long setorId;
    private String setorNome;
    private String setorSigla;

    // Escala resumida
    private Long escalaId;
    private String escalaNome;
    private String escalaTipo;
    private String horarioEntrada;
    private String horarioSaida;
    private String intervalo;
    private String diasTrabalho;
    private String folgas;

    // Benefícios
    private Set<BeneficioDTO> beneficios = new HashSet<>();

    // Usuário de acesso
    private Long usuarioId;
    private String username;
    private Role usuarioRole;
    private Boolean usuarioAtivo;

    public static class BeneficioDTO {
        private Long id;
        private String nome;
        private String categoria;
        private String descricao;
        private BigDecimal valor;
        private String informacoesImportantes;

        public BeneficioDTO() {}

        public BeneficioDTO(Beneficio b) {
            this.id = b.getId();
            this.nome = b.getNome();
            this.categoria = b.getCategoria();
            this.descricao = b.getDescricao();
            this.valor = b.getValor();
            this.informacoesImportantes = b.getInformacoesImportantes();
        }

        public Long getId() { return id; }
        public String getNome() { return nome; }
        public String getCategoria() { return categoria; }
        public String getDescricao() { return descricao; }
        public BigDecimal getValor() { return valor; }
        public String getInformacoesImportantes() { return informacoesImportantes; }
    }

    public FuncionarioResponse() {}

    public static FuncionarioResponse fromEntity(Funcionario f) {
        FuncionarioResponse r = new FuncionarioResponse();
        r.setId(f.getId());
        r.setMatricula(f.getMatricula());
        r.setNome(f.getNome());
        r.setEmail(f.getEmail());
        r.setTelefone(f.getTelefone());
        r.setCpf(f.getCpf());
        r.setDataNascimento(f.getDataNascimento());
        r.setDataAdmissao(f.getDataAdmissao());
        r.setStatus(f.getStatus());
        r.setTipoContrato(f.getTipoContrato());
        r.setObservacoes(f.getObservacoes());

        if (f.getCargo() != null) {
            r.setCargoId(f.getCargo().getId());
            r.setCargoNome(f.getCargo().getNome());
            r.setCargoNivel(f.getCargo().getNivel());
            r.setSalarioBase(f.getCargo().getSalarioBase());
        }

        if (f.getSetor() != null) {
            r.setSetorId(f.getSetor().getId());
            r.setSetorNome(f.getSetor().getNome());
            r.setSetorSigla(f.getSetor().getSigla());
        }

        if (f.getEscala() != null) {
            r.setEscalaId(f.getEscala().getId());
            r.setEscalaNome(f.getEscala().getNome());
            r.setEscalaTipo(f.getEscala().getTipoEscala());
            r.setHorarioEntrada(f.getEscala().getHorarioEntrada());
            r.setHorarioSaida(f.getEscala().getHorarioSaida());
            r.setIntervalo(f.getEscala().getIntervalo());
            r.setDiasTrabalho(f.getEscala().getDiasTrabalho());
            r.setFolgas(f.getEscala().getFolgas());
        }

        if (f.getBeneficios() != null) {
            r.setBeneficios(f.getBeneficios().stream()
                    .map(BeneficioDTO::new)
                    .collect(Collectors.toSet()));
        }

        if (f.getUsuario() != null) {
            r.setUsuarioId(f.getUsuario().getId());
            r.setUsername(f.getUsuario().getUsername());
            r.setUsuarioRole(f.getUsuario().getRole());
            r.setUsuarioAtivo(f.getUsuario().getAtivo());
        }

        return r;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getMatricula() { return matricula; }
    public void setMatricula(String matricula) { this.matricula = matricula; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }

    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }

    public LocalDate getDataNascimento() { return dataNascimento; }
    public void setDataNascimento(LocalDate dataNascimento) { this.dataNascimento = dataNascimento; }

    public LocalDate getDataAdmissao() { return dataAdmissao; }
    public void setDataAdmissao(LocalDate dataAdmissao) { this.dataAdmissao = dataAdmissao; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getTipoContrato() { return tipoContrato; }
    public void setTipoContrato(String tipoContrato) { this.tipoContrato = tipoContrato; }

    public String getObservacoes() { return observacoes; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }

    public Long getCargoId() { return cargoId; }
    public void setCargoId(Long cargoId) { this.cargoId = cargoId; }

    public String getCargoNome() { return cargoNome; }
    public void setCargoNome(String cargoNome) { this.cargoNome = cargoNome; }

    public String getCargoNivel() { return cargoNivel; }
    public void setCargoNivel(String cargoNivel) { this.cargoNivel = cargoNivel; }

    public BigDecimal getSalarioBase() { return salarioBase; }
    public void setSalarioBase(BigDecimal salarioBase) { this.salarioBase = salarioBase; }

    public Long getSetorId() { return setorId; }
    public void setSetorId(Long setorId) { this.setorId = setorId; }

    public String getSetorNome() { return setorNome; }
    public void setSetorNome(String setorNome) { this.setorNome = setorNome; }

    public String getSetorSigla() { return setorSigla; }
    public void setSetorSigla(String setorSigla) { this.setorSigla = setorSigla; }

    public Long getEscalaId() { return escalaId; }
    public void setEscalaId(Long escalaId) { this.escalaId = escalaId; }

    public String getEscalaNome() { return escalaNome; }
    public void setEscalaNome(String escalaNome) { this.escalaNome = escalaNome; }

    public String getEscalaTipo() { return escalaTipo; }
    public void setEscalaTipo(String escalaTipo) { this.escalaTipo = escalaTipo; }

    public String getHorarioEntrada() { return horarioEntrada; }
    public void setHorarioEntrada(String horarioEntrada) { this.horarioEntrada = horarioEntrada; }

    public String getHorarioSaida() { return horarioSaida; }
    public void setHorarioSaida(String horarioSaida) { this.horarioSaida = horarioSaida; }

    public String getIntervalo() { return intervalo; }
    public void setIntervalo(String intervalo) { this.intervalo = intervalo; }

    public String getDiasTrabalho() { return diasTrabalho; }
    public void setDiasTrabalho(String diasTrabalho) { this.diasTrabalho = diasTrabalho; }

    public String getFolgas() { return folgas; }
    public void setFolgas(String folgas) { this.folgas = folgas; }

    public Set<BeneficioDTO> getBeneficios() { return beneficios; }
    public void setBeneficios(Set<BeneficioDTO> beneficios) { this.beneficios = beneficios; }

    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public Role getUsuarioRole() { return usuarioRole; }
    public void setUsuarioRole(Role usuarioRole) { this.usuarioRole = usuarioRole; }

    public Boolean getUsuarioAtivo() { return usuarioAtivo; }
    public void setUsuarioAtivo(Boolean usuarioAtivo) { this.usuarioAtivo = usuarioAtivo; }
}
