package com.gruponexus.gestao.dto.request;

import com.gruponexus.gestao.entity.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Set;

public class FuncionarioRequest {

    @NotBlank(message = "A matrícula é obrigatória")
    private String matricula;

    @NotBlank(message = "O nome é obrigatório")
    private String nome;

    @NotBlank(message = "O e-mail é obrigatório")
    @Email(message = "E-mail inválido")
    private String email;

    private String telefone;
    private String cpf;
    private LocalDate dataNascimento;

    @NotNull(message = "A data de admissão é obrigatória")
    private LocalDate dataAdmissao;

    private String status = "ATIVO";
    private String tipoContrato = "CLT";

    @NotNull(message = "O cargo é obrigatório")
    private Long cargoId;

    @NotNull(message = "O setor é obrigatório")
    private Long setorId;

    private Long escalaId;
    private Set<Long> beneficioIds;

    // Criação automática de usuário de acesso
    private Boolean criarUsuarioAcesso = true;
    private Role roleUsuario = Role.ROLE_FUNCIONARIO;
    private String senhaInicial;

    private String observacoes;

    public FuncionarioRequest() {}

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public LocalDate getDataAdmissao() {
        return dataAdmissao;
    }

    public void setDataAdmissao(LocalDate dataAdmissao) {
        this.dataAdmissao = dataAdmissao;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTipoContrato() {
        return tipoContrato;
    }

    public void setTipoContrato(String tipoContrato) {
        this.tipoContrato = tipoContrato;
    }

    public Long getCargoId() {
        return cargoId;
    }

    public void setCargoId(Long cargoId) {
        this.cargoId = cargoId;
    }

    public Long getSetorId() {
        return setorId;
    }

    public void setSetorId(Long setorId) {
        this.setorId = setorId;
    }

    public Long getEscalaId() {
        return escalaId;
    }

    public void setEscalaId(Long escalaId) {
        this.escalaId = escalaId;
    }

    public Set<Long> getBeneficioIds() {
        return beneficioIds;
    }

    public void setBeneficioIds(Set<Long> beneficioIds) {
        this.beneficioIds = beneficioIds;
    }

    public Boolean getCriarUsuarioAcesso() {
        return criarUsuarioAcesso;
    }

    public void setCriarUsuarioAcesso(Boolean criarUsuarioAcesso) {
        this.criarUsuarioAcesso = criarUsuarioAcesso;
    }

    public Role getRoleUsuario() {
        return roleUsuario;
    }

    public void setRoleUsuario(Role roleUsuario) {
        this.roleUsuario = roleUsuario;
    }

    public String getSenhaInicial() {
        return senhaInicial;
    }

    public void setSenhaInicial(String senhaInicial) {
        this.senhaInicial = senhaInicial;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }
}
