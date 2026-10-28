package com.gruponexus.gestao.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.math.BigDecimal;

@Entity
@Table(name = "cargos")
public class Cargo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O nome do cargo é obrigatório")
    @Column(nullable = false, length = 100)
    private String nome;

    @Column(length = 50)
    private String nivel; // Júnior, Pleno, Sênior, Especialista, Coordenação, Gerência, Diretoria

    @Column(precision = 10, scale = 2)
    private BigDecimal salarioBase;

    @Column(length = 255)
    private String descricao;

    @Column(nullable = false)
    private Boolean ativo = true;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "setor_id")
    @JsonIgnoreProperties({"funcionarios", "cargos"})
    private Setor setor;

    public Cargo() {}

    public Cargo(String nome, String nivel, BigDecimal salarioBase, String descricao, Setor setor) {
        this.nome = nome;
        this.nivel = nivel;
        this.salarioBase = salarioBase;
        this.descricao = descricao;
        this.setor = setor;
        this.ativo = true;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getNivel() {
        return nivel;
    }

    public void setNivel(String nivel) {
        this.nivel = nivel;
    }

    public BigDecimal getSalarioBase() {
        return salarioBase;
    }

    public void setSalarioBase(BigDecimal salarioBase) {
        this.salarioBase = salarioBase;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public Setor getSetor() {
        return setor;
    }

    public void setSetor(Setor setor) {
        this.setor = setor;
    }
}
