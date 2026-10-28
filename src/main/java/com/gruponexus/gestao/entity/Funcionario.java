package com.gruponexus.gestao.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "funcionarios")
public class Funcionario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "A matrícula é obrigatória")
    @Column(nullable = false, unique = true, length = 30)
    private String matricula;

    @NotBlank(message = "O nome completo é obrigatório")
    @Column(nullable = false, length = 120)
    private String nome;

    @NotBlank(message = "O e-mail é obrigatório")
    @Email(message = "E-mail com formato inválido")
    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(length = 25)
    private String telefone;

    @Column(length = 20)
    private String cpf;

    private LocalDate dataNascimento;

    @NotNull(message = "A data de admissão é obrigatória")
    @Column(nullable = false)
    private LocalDate dataAdmissao;

    @Column(nullable = false, length = 30)
    private String status = "ATIVO"; // ATIVO, INATIVO, FERIAS, AFASTADO

    @Column(length = 30)
    private String tipoContrato = "CLT"; // CLT, PJ, ESTAGIO, TEMPORARIO

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cargo_id")
    @JsonIgnoreProperties({"funcionarios"})
    private Cargo cargo;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "setor_id")
    @JsonIgnoreProperties({"funcionarios", "cargos"})
    private Setor setor;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "escala_id")
    private Escala escala;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "funcionario_beneficios",
        joinColumns = @JoinColumn(name = "funcionario_id"),
        inverseJoinColumns = @JoinColumn(name = "beneficio_id")
    )
    @JsonIgnoreProperties({"funcionarios"})
    private Set<Beneficio> beneficios = new HashSet<>();

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    @JsonIgnoreProperties({"funcionario", "senha"})
    private Usuario usuario;

    @Column(columnDefinition = "TEXT")
    private String observacoes;

    public Funcionario() {}

    public Funcionario(String matricula, String nome, String email, String telefone, String cpf, 
                       LocalDate dataAdmissao, Cargo cargo, Setor setor, Escala escala) {
        this.matricula = matricula;
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
        this.cpf = cpf;
        this.dataAdmissao = dataAdmissao;
        this.cargo = cargo;
        this.setor = setor;
        this.escala = escala;
        this.status = "ATIVO";
        this.tipoContrato = "CLT";
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public Cargo getCargo() {
        return cargo;
    }

    public void setCargo(Cargo cargo) {
        this.cargo = cargo;
    }

    public Setor getSetor() {
        return setor;
    }

    public void setSetor(Setor setor) {
        this.setor = setor;
    }

    public Escala getEscala() {
        return escala;
    }

    public void setEscala(Escala escala) {
        this.escala = escala;
    }

    public Set<Beneficio> getBeneficios() {
        return beneficios;
    }

    public void setBeneficios(Set<Beneficio> beneficios) {
        this.beneficios = beneficios;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }
}
